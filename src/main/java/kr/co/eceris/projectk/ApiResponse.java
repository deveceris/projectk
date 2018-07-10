package kr.co.eceris.projectk;

public final class ApiResponse {

	private String message;

	private Object data;

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public static ApiResponse data(Object data) {
		return new Builder().data(data).build();
	}

	public static ApiResponse message(String message) {
		return new Builder().message(message).build();
	}

	public static class Builder {
		private Object data;
		private String message;

		public Builder data(Object data) {
			this.data = data;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public ApiResponse build() {
			return new ApiResponse(this);
		}
	}

	public ApiResponse(Builder builder) {
		this.data = builder.data;
		this.message = builder.message;
	}
}
