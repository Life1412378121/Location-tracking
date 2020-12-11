package com.example.mlocation.been;

public class BaiDuMapByIp {
	public String address;
	public String status;
	public Content content;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public class Content {
		public Address_detail address_detail;
		public String  address;
		public Point point;

		public Address_detail getAddress_detail() {
			return address_detail;
		}

		public void setAddress_detail(Address_detail address_detail) {
			this.address_detail = address_detail;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public class Address_detail {
			public String province;
			public String city;
			public String district;
			public String street;
			public String street_number;
			public int city_code;

			public String getProvince() {
				return province;
			}

			public void setProvince(String province) {
				this.province = province;
			}

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public String getDistrict() {
				return district;
			}

			public void setDistrict(String district) {
				this.district = district;
			}

			public String getStreet() {
				return street;
			}

			public void setStreet(String street) {
				this.street = street;
			}

			public String getStreet_number() {
				return street_number;
			}

			public void setStreet_number(String street_number) {
				this.street_number = street_number;
			}

			public int getCity_code() {
				return city_code;
			}

			public void setCity_code(int city_code) {
				this.city_code = city_code;
			}

		}

		public class Point {
			public String y;
			public String x;
			public String getY() {
				return y;
			}
			public void setY(String y) {
				this.y = y;
			}
			public String getX() {
				return x;
			}
			public void setX(String x) {
				this.x = x;
			}
		}
	}
}
