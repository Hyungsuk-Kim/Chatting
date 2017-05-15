import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

class ChatRoomDisplay extends JFrame implements ActionListener, KeyListener,
		ListSelectionListener, ChangeListener {
	private ClientThread cr_thread; // 클라이언트 쓰레드
	private String idTo; // 닉네임 전송
	private boolean isSelected; // 선택 여부 확인
	public boolean isAdmin; // admin 여부 확인

	private JLabel roomer; // 접속자 레이블
	public JList roomerInfo; // 접속자 리스트
	private JButton coerceOut, sendWord, sendFile, quitRoom; // 버튼(강제퇴장, 귓속말보내기, 파일전송, 퇴실하기)
	private Font font; // 글꼴
	private JViewport view; //
	private JScrollPane jsp3; // 스크롤바
	public JTextArea messages; // 텍스트 출력창
	public JTextField message; // 텍스트 입력 필드

	public ChatRoomDisplay(ClientThread thread) {
		super("Chat-Application-대화방"); // 폼 네임 지정

		cr_thread = thread; // cr_thread에 쓰레드 할당
		isSelected = false; // 선택 여부 default로 false 지정
		isAdmin = false; //
		font = new Font("SanSerif", Font.PLAIN, 12); // 폰트 설정

		Container c = getContentPane(); // 컨테이너 할당
		c.setLayout(null); // 레이아웃을 null로 지정

		JPanel p = new JPanel(); // Panel 생성
		p.setLayout(null); // 레이아웃을 null로 지정
		p.setBounds(425, 10, 140, 175); // Panel을 해당 좌표에 위치시킴
		p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
				"참여자")); // 테두리 설정

		roomerInfo = new JList(); // 리스트 생성
		roomerInfo.setFont(font); // 글꼴 지정
		JScrollPane jsp2 = new JScrollPane(roomerInfo); // roomerInfo에 스크롤바 지정
		roomerInfo.addListSelectionListener(this); // 리스트에 리스너 등록
		jsp2.setBounds(15, 25, 110, 135); // 스크롤바의 위치 지정
		p.add(jsp2); // 스크롤바 등록
		c.add(p); // 컨테이너 등록 

		p = new JPanel(); // Panel 생성
		p.setLayout(null); // 레이아웃을 null로 지정
		p.setBounds(10, 10, 410, 340); // Panel을 해당 좌표에 위치시킴
		p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
				"채팅창")); // 테두리 설정

		view = new JViewport(); //
		messages = new JTextArea(); // 메시지 출력 
		messages.setFont(font); // messages의 글꼴 지정 
		messages.setEditable(false); // TextArea의 내용을 수정하지 못하도록 정 
		view.add(messages); // view에 messages 등록 
		view.addChangeListener(this); // view에 리스너 등록 
		jsp3 = new JScrollPane(view); // view에 스크롤바 등록 
		jsp3.setBounds(15, 25, 380, 270); // 채팅창 위치 및 크기
		p.add(jsp3); // 스크롤바 추가

		message = new JTextField(); // 텍스트 입력창 생성
		message.setFont(font); // 글꼴 출력
		message.addKeyListener(this); //
		message.setBounds(15, 305, 380, 20); // 텍스트 입력창 위치 및 크기
		message.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED)); // 테두리 설정 
		p.add(message); // Panel에 TextField 등록 
		c.add(p); // 컨테이너에 Panel 등록 

		coerceOut = new JButton("강 제 퇴 장"); // 강제퇴장 버튼 생성
		coerceOut.setFont(font); // 글꼴 지정 
		coerceOut.addActionListener(this); // 버튼에 리스너 등록 
		coerceOut.setBounds(445, 195, 100, 30); // 강제퇴장 버튼 위치 및 크기
		coerceOut.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); // 테두리 설정 
		c.add(coerceOut); // 컨테이너에 강제퇴장 버튼 추가

		sendWord = new JButton("귓속말보내기"); // 귓속말보내기 버튼 생성
		sendWord.setFont(font); // 글꼴 지정 
		sendWord.addActionListener(this); // 버튼에 리스너 등록 
		sendWord.setBounds(445, 235, 100, 30); // 귓속말보내기 버튼 위치 및 크기
		sendWord.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); // 테두리 설정 
		c.add(sendWord); // 컨테이너에 귓속말보내기 버튼 추가

		sendFile = new JButton("파 일 전 송"); // 파일전송 버튼 생성
		sendFile.setFont(font); // 글꼴 지정 
		sendFile.addActionListener(this); // 버튼에 리스너 등록 
		sendFile.setBounds(445, 275, 100, 30); // 파일전송 버튼 위치 및 크기
		sendFile.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); // 테두리 설정 
		c.add(sendFile); // 파일전송 버튼 추가

		quitRoom = new JButton("퇴 실 하 기"); // 퇴실하기 버튼 생성
		quitRoom.setFont(font); // 글꼴 출력
		quitRoom.addActionListener(this); // 버튼에 리스너 등록 
		quitRoom.setBounds(445, 315, 100, 30); // 퇴실하기 버튼 위치 및 크기
		quitRoom.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); // 테두리 설정 
		c.add(quitRoom); // 퇴실하기 버튼 추가

		Dimension dim = getToolkit().getScreenSize(); //
		setSize(580, 400); // 윈도우 크기 설정
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2
				- getHeight() / 2); // 윈도우의 위치 설정
		show(); // 윈도우를 보여준다.

		addWindowListener(new WindowAdapter() { // 윈도우 리스너 추가 
			public void windowActivated(WindowEvent e) { // 윈도우 창이 켜졌을떄
				message.requestFocusInWindow(); //
			}
		});

		addWindowListener(new WindowAdapter() { //
			public void windowClosing(WindowEvent e) { // 윈도우 창 닫힘 
				cr_thread.requestQuitRoom(); // cr_thread를 실행에서 제거 하는거
			}
		});
	}

	public void resetComponents() {
		messages.setText(""); //
		message.setText(""); //
		message.requestFocusInWindow(); // 
	}

	public void keyPressed(KeyEvent ke) { // 눌렀을 때 나타나는 이벤트 설정
		if (ke.getKeyChar() == KeyEvent.VK_ENTER) { // 엔터키를 눌렀을 때
			String words = message.getText(); // word에 메시지를 할당 
			String data; // 데이터 
			String idTo; // 전송할 아이디
			if (words.startsWith("/w")) { //
				StringTokenizer st = new StringTokenizer(words, " "); //
				String command = st.nextToken(); //
				idTo = st.nextToken(); //
				data = st.nextToken(); //
				cr_thread.requestSendWordTo(data, idTo); //
				message.setText(""); //
			} else {
				cr_thread.requestSendWord(words); //
				message.requestFocusInWindow(); //
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) { // 리스트 값이 변화 되었을때
		isSelected = true; // 항목이 선택되었을때
		idTo = String.valueOf(((JList) e.getSource()).getSelectedValue()); // 
	}

	public void actionPerformed(ActionEvent ae) { //
		if (ae.getSource() == coerceOut) { //
			if (!isAdmin) { //
				JOptionPane.showMessageDialog(this, "당신은 방장이 아닙니다.", "강제퇴장",
						JOptionPane.ERROR_MESSAGE); // 방장이 아니였을 때, 강제퇴장 버튼을 누르면
													// 에러메세지 출력.
			} else if (!isSelected) {
				JOptionPane.showMessageDialog(this, "강제퇴장 ID를 선택하세요.", "강제퇴장",
						JOptionPane.ERROR_MESSAGE); // 방장인데 아이디를 선택하지 않고 강제퇴장
													// 버튼을 누르면 에러메세지 출력.
			} else {
				cr_thread.requestCoerceOut(idTo); // 선택한 아이디를 퇴장시킴
				isSelected = false; // 선택 해제
			}
		} else if (ae.getSource() == quitRoom) { //
			cr_thread.requestQuitRoom(); //
		} else if (ae.getSource() == sendWord) { //
			String idTo, data; // 아이디, 데이터 받는다.
			if ((idTo = JOptionPane.showInputDialog("아이디를 입력하세요.")) != null) { //
				if ((data = JOptionPane.showInputDialog("메세지를 입력하세요.")) != null) { //
					cr_thread.requestSendWordTo(data, idTo); //
				}
			}
		} else if (ae.getSource() == sendFile) { //
			String idTo; //
			if ((idTo = JOptionPane.showInputDialog("상대방 아이디를 입력하세요.")) != null) { //
				cr_thread.requestSendFile(idTo); //
			}
		}
	}

	public void stateChanged(ChangeEvent e) { //
		jsp3.getVerticalScrollBar().setValue(
				(jsp3.getVerticalScrollBar().getValue() + 20)); //
	}

	public void keyReleased(KeyEvent e) {
	} //

	public void keyTyped(KeyEvent e) {
	} //
}