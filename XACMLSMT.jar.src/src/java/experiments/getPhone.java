package experiments;

public class getPhone {

	public static void main(String[] args) {
		String[] arrPhone = { "0762 898 272", "0949 034 068" };
		for (int i = 0; i < arrPhone.length; i++) {
			System.out.println("Phone before hide: " + arrPhone[i]);

			String phone = cover(arrPhone[i]);
			System.out.println("Phone after hide:  " + phone);
			System.out.println("------------------------------------------------");
		}
	}

	public static String cover(String phone) {
		/* Cut 3 phone number */
		String headPhone = phone.substring(0, 3);
		/* Cut Phone Number from Character 3 */
		String coverPhone = phone.substring(3);
		for (int i = 0; i < coverPhone.length(); i++) {
			/*check character in String follow value "i"*/
			char c = coverPhone.charAt(i);

			/* replace character with '*' */
			coverPhone = coverPhone.replace(c, '*');
		}
		phone = headPhone + coverPhone;
		return phone;
	}

}
