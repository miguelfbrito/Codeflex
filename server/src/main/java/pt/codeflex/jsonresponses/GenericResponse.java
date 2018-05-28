package pt.codeflex.jsonresponses;

public class GenericResponse {

	private Object object;
	private String message;

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

}
