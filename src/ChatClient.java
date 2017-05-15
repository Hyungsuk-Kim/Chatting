import javax.swing.*;

public class ChatClient {
	public static String getLogonID() { // logonID를 입력받아 리턴함(static 메소드로 인스턴스가 없어도 실행 가능)

		String logonID = "";
		try {
			while (logonID.equals("")) {
				logonID = JOptionPane.showInputDialog("로그온 ID를 입력하세요.");
			}
		} catch (NullPointerException e) {
			System.exit(0); // NullPointException 발생 시 종료
		}
		return logonID;
	}

	public static void main(String args[]) {
		String id = getLogonID(); // id에 logonID 할당
		try {
			if (args.length == 0) { // args에 아무것도 없을 시, ClientThread()를 호출
				ClientThread thread = new ClientThread();
				thread.start();
				thread.requestLogon(id); // id를 가지고 로그온 요청
			} else if (args.length == 1) { // args가 있을 시 ClientThread(String hostaddr)를 호출
				ClientThread thread = new ClientThread(args[0]);
				thread.start();
				thread.requestLogon(id);// id를 가지고 로그온 요청
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
