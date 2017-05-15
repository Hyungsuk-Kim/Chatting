import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

class MessageBoxLess extends JDialog implements ActionListener {
	private Frame client; // client 프레임
	private Container c; // 컨테이너
	
	public MessageBoxLess(JFrame parent, String title, String message){
		super(parent, true); // 슈퍼클래스(JDialog)의 생성자 호출
		setTitle(title); // 타이틀 지정
		c = getContentPane(); //
		c.setLayout(null); // 레이아웃 없음
		JLabel lbl = new JLabel(message); // message로 레이블 생성
		lbl.setFont(new Font("SanSerif", Font.PLAIN, 12)); // 레이블의 글꼴 설정
		lbl.setBounds(20, 10, 190, 20); // 레이블을 해당 좌표에 위치시킴
		c.add(lbl); // 컨테이너에 레이블 등록
		
		JButton bt = new JButton("O K"); // "O K" 버튼 생성
		bt.setBounds(60, 40, 70, 25); // 버튼을 해당 좌표에 위치시킴
		bt.setFont(new Font("SanSerif", Font.PLAIN, 12)); // 버튼의 글꼴 설정
		bt.addActionListener(this); // 버튼에 이벤트 리스너 등록
		bt.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); // 테두리 지정
		c.add(bt); // 컨테이너에 버튼 등록
		
		Dimension dim = getToolkit().getScreenSize(); //
		setSize(200, 100); //
		setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2); //
		show(); //
		client = parent; //
	}
	
	public void actionPerformed(ActionEvent ae){
		dispose(); // 컨테이너가 점유하던 메모리 반납
		System.exit(0); // 프로그램 종료
	}
}
