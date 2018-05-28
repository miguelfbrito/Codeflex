package pt.codeflex.jsonresponses;

public class GenericResponse {

	private Object object;
	private int code;
	private String message;

	public GenericResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public GenericResponse(int code, Object object, String message) {
		this.code = code;
		this.message = message;
		this.object = object;
	}

	public GenericResponse(Object object, String message) {
		super();
		this.object = object;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
