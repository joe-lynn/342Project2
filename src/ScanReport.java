

public class ScanReport {

  private final Passenger person;
  private final boolean passed;

  public ScanReport(Passenger subject, boolean result) {
    person = subject;
    this.passed = result;
  }

  public Passenger getSubject() {
    return person;
  }

  public boolean getResult() {
    return passed;
  }
}
