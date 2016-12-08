

/**
* The message class used by the scanners to report to the security station.
*/
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

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof ScanReport) {
      if(person.equals(((ScanReport)obj).getSubject())) {
	    return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return person.hashCode();
  }
}
