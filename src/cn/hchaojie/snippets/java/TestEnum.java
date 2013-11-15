package cn.hchaojie.snippets.java;


public class TestEnum {
	public enum Type {
		CALL("call"),
		TWITTER("twitter"),
		FACEBOOK("facebook"),
		MESSAGE("message");
		
		private String typeText;
		private Type(String typeText) {
			this.typeText = typeText;
		}
		
		public String getTypeText() {
			return this.typeText;
		}
		
		public static Type getType(String typeText) {
			Type[] types = Type.values();
			for (Type t : types) {
				String text = t.getTypeText();
				if (text.equalsIgnoreCase(typeText)) {
					return t;
				}
			}
			
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Type.getType("call").name());
	}

}
