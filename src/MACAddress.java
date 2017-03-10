import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class MACAddress {

	String ip;
	String mac;
	public MACAddress (String ip){
		this.ip = ip;
	}
	public MACAddress (){
		this.ip = "127.0.0.1";
	}
	public String getMac(){
		try {
		Process p = Runtime.getRuntime().exec("arp -n");
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			p.waitFor();
			boolean flag = true;
			String ipStr = "(" + this.ip + ")";
			while(flag) {
				String str = input.readLine();
				if (str != null) {
					if (str.indexOf(ipStr) > 1) {
						int temp = str.indexOf("at");
						this.mac = str.substring(
						temp + 3, temp + 20);
						break;
					}
				} else
				flag = false;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace(System.out);
		}
		return this.mac;
	}
	public void setIp(String ip){
		this.ip = ip;
	}
	public static void main(String []args){
		 MACAddress m = new MACAddress();
		 System.out.println(m.getMac());
	}
}
