package kisung.moin.enums;

public enum IdType {
  REG_NO("REG_NO"),
  BUSINESS_NO("BUSINESS_NO");

  String status;

  IdType(String status) {
    this.status = status;
  }

  public String value() {
    return status;
  }
}
