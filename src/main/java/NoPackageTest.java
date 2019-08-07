import org.testng.annotations.Test;
public class NoPackageTest {
@Test
  public void testThis() throws Exception {
	throw new Exception("this hasn't been done yet");
  }
}
