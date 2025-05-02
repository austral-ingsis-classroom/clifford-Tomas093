package edu.austral.ingsis.clifford.commands.types;

public enum LsOrder {
  ASCENDING("asc"),
  DESCENDING("desc"),
  DEFAULT("");

  private final String value;

  LsOrder(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static LsOrder fromString(String value) {
    for (LsOrder type : LsOrder.values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    return DEFAULT;
  }
}
