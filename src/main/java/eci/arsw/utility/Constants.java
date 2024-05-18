package eci.arsw.utility;

public class Constants {

	public enum UserRole {
		ROLE_CUSTOMER("Customer");

		private String role;

		private UserRole(String role) {
			this.role = role;
		}

		public String value() {
			return this.role;
		}
	}
	
	public enum ProductStatus {
		AVAILABLE("Available"), DEACTIVATED("Deactivated"), SOLD("Sold"), UNSOLD("UnSold");

		private String status;

		private ProductStatus(String status) {
			this.status = status;
		}

		
		public String value() {
			return this.status;
		}
	}

	public enum ProductOfferStatus {
		ACTIVE("Active"), CANCELLED("Cancelled"), WIN("Win"), LOSE("Lose");

		private String status;

		private ProductOfferStatus(String status) {
			this.status = status;
		}

		public String value() {
			return this.status;
		}
	}

}
