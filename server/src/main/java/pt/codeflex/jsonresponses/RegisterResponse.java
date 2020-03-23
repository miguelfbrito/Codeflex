package pt.codeflex.jsonresponses;

public class RegisterResponse {

	/*
	 * CODE 0 - username already in use
	 * CODE 1 - email already in use
	 * CODE 2 - username and email already in use
	 */
	private int code;
	private String message;
	
	public RegisterResponse( int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
