package experiments;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.MongoServerException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class getDataMongoDB {

	public static void main(String[] args) throws MongoServerException {
		/* Get data value from MOCK_DATA Collection */
//		enableHideFunction();

		/* Get data value from SubjectDetail Collection */
//		getValueCollectionJoin();

		/* Function Interactive MongoDB */
//		Ping();
//		InsertData();
//		FindAllObjectInCollection();
//		FindDataWithClase("Teacher", "teacher_01");
		String[] Field = { "Teacher" };
		String[] Value = { "teacher_01", "teacher_02" };
		FindDataWithClaseArray(Field, Value);
	}

	private static void enableHideFunction() {
		/* Connect to Database */
		try (MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("AccessControl");
			System.out.println("Connect to database Successfully");
			/* Get Collection */
			MongoCollection<Document> collection = database.getCollection("MOCK_DATA");

			Document query = new Document();
//			query.append("gender", "Male");

			Document projection = new Document();
			projection.append("gender", "$gender");
			projection.append("email", "$email");
			projection.append("phone", "$phone");

			Block<Document> processBlock = new Block<Document>() {
				@Override
				public void apply(final Document document) {
					System.out.println(document);

					String gender = document.getString("gender");
					String phone = document.getString("phone");
					String mail = document.getString("email");

					if (gender.equals("Male")) {
						phone = getPhone.cover(phone);
						System.out.println("\t\t Sau khi thay đổi" + "\t" + "Phone= " + phone + ", Email= " + mail);
					} else {
						mail = getEmail.cover(mail);
						System.out.println("\t\t Sau khi thay đổi" + "\t" + "Email= " + mail + ", Phone= " + phone);
					}
					System.out.println("---------------------------------------------------------------------------");
				}
			};
			collection.find(query).projection(projection).forEach(processBlock);

		} catch (MongoException e) {
			// handle MongoDB exception
		}
	}

	private static void getValueCollectionJoin() {
		try (MongoClient client = new MongoClient("localhost", 27017))
		{
			MongoDatabase database = client.getDatabase("AccessControl");
			MongoCollection<Document> collection = database.getCollection("SubjectDetail");

			Block<Document> processBlock = new Block<Document>() {
				@Override
				public void apply(final Document document) {
					System.out.println(document);
				}
			};

			List<? extends Bson> pipeline = Arrays.asList(
					new Document().append("$project", new Document().append("_id", 0).append("SubjectDetail", "$$ROOT")),
					new Document().append("$lookup",
							new Document().append("localField", "SubjectDetail.Name").append("from", "Subject")
									.append("foreignField", "id_subject").append("as", "Subject")),
					new Document().append("$unwind",
							new Document().append("path", "$Subject").append("preserveNullAndEmptyArrays", false)));
			collection.aggregate(pipeline).allowDiskUse(true).forEach(processBlock);

			} catch (MongoException e) {
			// handle MongoDB exception
			}
	}

	/* =================== Connection =================== */

	/* Connect MongoDB not security */
	private static MongoClient getMongoClientbyDefaultRole() {
		MongoClient mongoClient = new MongoClient(MyConstants.HOST, MyConstants.PORT);
		return mongoClient;
	}

	/* Connect MongoDB Security */
	private static MongoClient getMongoClientByUsers() {
		final String USERNAME = "admin";
		final String PASSWORD = "123123";
		MongoCredential mongoCredential = MongoCredential.createCredential(USERNAME, MyConstants.DB_NAME,
				PASSWORD.toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(MyConstants.HOST, MyConstants.PORT),
				Arrays.asList(mongoCredential));
		return mongoClient;
	}

	/* ========================================================= */

	/* Connect to Mongo Database */
	public static void Ping() throws UnknownHostException {
		MongoClient mongoClient = getMongoClientbyDefaultRole();
		System.out.println("List all DB");

		List<String> dbNames = mongoClient.getDatabaseNames();
		for (String dbName : dbNames) {
			System.out.println("DB Name: " + dbName);
		}
	}
	/* ========================================================= */

	/* Insert Value to Collection in Mongo Database */
	public static void InsertData() throws UnknownHostException {
		/* Connect to DB */
		MongoClient mongoClient = getMongoClientbyDefaultRole();
		DB db = mongoClient.getDB(MyConstants.DB_NAME);

		DBCollection dbCollection = db.getCollection("Subject");
		try {
			BasicDBObject object = new BasicDBObject();
			object.append("id_subject", "subject_03");
			object.append("Name", "Do an 2");
			object.append("Episode", 30);
			dbCollection.insert(object);
			System.out.println("=========================================================");
			System.out.println("\t\t Insert Successful");
			System.out.println("=========================================================");
		} catch (Exception e) {
			System.out.println("=========================================================");
			System.out.println("\t\t Insert Failed");
			System.out.println("=========================================================");
		}
	}

	/* String Query to Collection in MongoDB */

	public static void FindAllObjectInCollection() {
		MongoClient mongoClient = getMongoClientbyDefaultRole();
		
		DB db = mongoClient.getDB(MyConstants.DB_NAME);
		DBCollection dbCollection = db.getCollection("SubjectDetail");

		DBCursor cursor = dbCollection.find();
		int i = 0;
		while (cursor.hasNext()) {
			System.out.println("Document: " + i);
			System.out.println(cursor.next());
			i++;
		}
	}

	/***
	 * 
	 * @param Field: insert field you want
	 * @param Value: insert value of field you wanted
	 */
	private static DBObject getWhereClase(String Field, String Value) {
	BasicDBObjectBuilder whereBuilder = BasicDBObjectBuilder.start();
	
		whereBuilder.append(Field, Value);

		DBObject where = whereBuilder.get();
		System.out.println("where " + where.toString());
		return where;
	}

	/***
	 * 
	 * @param Field: insert field you want
	 * @param Value: insert value of field you wanted
	 */
	public static void FindDataWithClase(String Field, String Value) {
		/* Connect Database */
		MongoClient mongoClient = getMongoClientbyDefaultRole();
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB(MyConstants.DB_NAME);
		
		DBCollection dbCollection = db.getCollection("SubjectDetail");
		
		DBObject where = getWhereClase(Field, Value);
		/* Query with Clause */
		DBCursor dbCursor = dbCollection.find(where);

		int i = 1;
		while (dbCursor.hasNext()) {
			System.out.println("Document: " + i);
			System.out.println(dbCursor.next());
			i++;
		}
	}

	/***
	 * 
	 * @param Field: insert field you want
	 * @param Value: insert value of field you wanted
	 */
	private static DBObject getWhereClaseArray(String[] Field, String[] Value) {
		BasicDBObjectBuilder whereBuilder = BasicDBObjectBuilder.start();
		for (int i = 0; i < Field.length; i++) {
			String field = Field[i];
			for (int j = 0; j < Value.length; j++) {
				String value = Value[i];
				whereBuilder.append(field, value);
			}
		}
//		whereBuilder.append(Field, Value);

		DBObject where = whereBuilder.get();
		System.out.println("where " + where.toString());
		return where;
	}

	/***
	 * 
	 * @param Field: insert field you want
	 * @param Value: insert value of field you wanted
	 */
	public static void FindDataWithClaseArray(String[] Field, String[] Value) {
		/* Connect Database */
		MongoClient mongoClient = getMongoClientbyDefaultRole();
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB(MyConstants.DB_NAME);

		DBCollection dbCollection = db.getCollection("SubjectDetail");

		DBObject where = getWhereClaseArray(Field, Value);
		/* Query with Clause */
		DBCursor dbCursor = dbCollection.find(where);

		int i = 1;
		while (dbCursor.hasNext()) {
			System.out.println("Document: " + i);
			System.out.println(dbCursor.next());
			i++;
		}
	}

}