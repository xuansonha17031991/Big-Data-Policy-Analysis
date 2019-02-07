package experiments;

public class getEmail {

	public static void main(String[] args) {
		String[] arrEmail = { "lengoctienthanh@gmail.com", "1234567890@gmail.com" };
		for (int i = 0; i < arrEmail.length; i++) {
			System.out.println("Email before hide: " + arrEmail[i]);

			String mail = cover(arrEmail[i]);
			System.out.println("Email after hide:  " + mail);
			System.out.println("------------------------------------------------");
		}
	}

	public static String cover(String mail) {
		String[] coverEmail = mail.split("@");
		/*
		 * coverEmail[0]: value before @ ; coverEmail[1]: value after @ ;
		 */
		for (int i = 0; i < coverEmail[0].length(); i++) {
			/* check character in String follow value "i" */
			char c = coverEmail[0].charAt(i);

			/* replace character with '*' */
			coverEmail[0] = coverEmail[0].replace(c, '*');
		}
		mail = coverEmail[0] + "@" + coverEmail[1];
		return mail;
	}
}
