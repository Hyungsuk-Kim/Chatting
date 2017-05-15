import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class SendFile extends Frame implements ActionListener{
	private TextField tf_filename; //텍스트필드 변수
	private Button bt_dialog, bt_send, bt_close; //버튼 변수
	private Label lb_status; //레이블 변수
	
	private static final String SEPARATOR = "|"; //주고받는 데이터가 2가지 이상일때, 각각의 데이터를 구분하기위한 구분자
	private String address; //
	
	public SendFile(String address){
		super("파일전송"); //폼 네임 지정
		this.address = address; //
		
		setLayout(null); //레이아웃 매니저의 설정을 해제한다.
		
		Label lbl = new Label("파일이름"); //파일이름 레이블 생성
		lbl.setBounds(10, 30, 60, 20); //파일이름 위치 및 크기
		add(lbl); //파일이름 레이블 추가
		
		tf_filename = new TextField();//새로운 텍스트 입력란 생성
		tf_filename.setBounds(80, 30, 160, 20); //텍스트 입력란 위치 및 크기
		add(tf_filename); //텍스트 입력란 추가.
		
		bt_dialog = new Button("찾아보기"); //찾아보기 버튼 생성
		bt_dialog.setBounds(45, 60, 60, 20); //찾아보기 버튼 위치 및 크기
		bt_dialog.addActionListener(this); //버튼에 발생할 이벤트를 처리할 이벤트 리스너 객체를 연결한다.
		add(bt_dialog); //찾아보기 버튼 추가.
		
		bt_send = new Button("전송"); //전송 버튼 생성
		bt_send.setBounds(115, 60, 40, 20); //전송 버튼 위치 및 크기
		bt_send.addActionListener(this); //버튼에 발생할 이벤트를 처리할 이벤트 리스너 객체를 연결한다.
		add(bt_send); //전송 버튼 추가
		
		bt_close = new Button("종료"); //종료 버튼 생성
		bt_close.setBounds(165, 60, 40, 20); //종료 버튼 위치 및 크기
		bt_close.addActionListener(this); //버튼에 발생할 이벤트를 처리할 이벤트 리스너 객체를 연결한다.
		add(bt_close); //종료 버튼 추가
		
		lb_status = new Label("파일전송 대기중....."); //파일전송 대기중.....레이블 생성
		lb_status.setBounds(10, 90, 230, 20); //파일전송 대기중 레이블 위치 및 크기
		lb_status.setBackground(Color.gray); //파일전송 대기중 레이블 배경 색깔(회색) 지정
		lb_status.setForeground(Color.white); //파일전송 대기중 글짜색(흰색) 지정
		
		add(lb_status); //파일전송 대기중 레이블 추가
		
		addWindowListener(new WinListener()); //
		
		setSize(250, 130); //윈도우 크기 설정
		show(); //윈도우를 보여준다.
	}
	public void actionPerformed(ActionEvent e){ //
		if (e.getSource() == bt_dialog){ //
			FileDialog fd = new FileDialog(this, "파일 찾기", FileDialog.LOAD); //
			fd.show(); //
			tf_filename.setText(fd.getDirectory() + fd.getFile()); //파일의 저장 위치와 파일이름을 가져온다
			if (tf_filename.getText().startsWith("null")) //파일이름이 없다면
				tf_filename.setText(""); //파일이름이 없는 걸로 출력한다.
		}else if(e.getSource() == bt_send){ //
			String filename = tf_filename.getText(); //파일이름을 출력한다.
			
			if(filename.equals("")){ //보낼 파일 이름이 입력되어 있지 않으면
				lb_status.setText("파일이름을 입력하세요."); //파일이름을 입력하세요를 레이블에 출력한다.
				return; //
			}
			
			lb_status.setText("파일검색중..."); //
			
			File file = new File(filename); //
			
			if(!file.exists()) { //파일이 존재하지 않으면
				lb_status.setText("해당파일을 찾을 수 없습니다."); //해당파일을 찾을 수 없습니다.를 레이블에 출력한다.
				return; //
			}
			
			StringBuffer buffer = new StringBuffer(); //
			int fileLength = (int) file.length(); //
			
			buffer.append(file.getName()); //
			buffer.append(SEPARATOR); //
			buffer.append(fileLength); //
			
			lb_status.setText("연결설정중..."); //
			
			try{
				Socket sock = new Socket(address, 3777); //3777번 포트를 사용하는 소켓 생성
				FileInputStream fin = new FileInputStream(file); //파일에서 데이터를 읽어 들이기 위한 FileInputStream 객체 생성   
				BufferedInputStream bin = new BufferedInputStream(fin, fileLength); //
				byte data[] = new byte[fileLength]; //
				try{
					lb_status.setText("전송할 파일 로드 중..."); //
					bin.read(data, 0, fileLength); //
					bin.close(); //
				}catch(IOException err){ //
					lb_status.setText("파일읽기 오류"); //
					return; //
				}
				
				/*
				 * for(int i=0; i<data.length; i++){
				 * 	System.out.print(data[i]);
				 * }
				*/
				
				DataOutputStream out = new DataOutputStream(sock.getOutputStream()); //ct_sock클래스의 getOutputStream 메소드를 출력
				out.writeUTF(buffer.toString()); //데이터 출력스트림에서 수정된 UTF-8형식을 사용하여 인코드된 문자열로 읽어와 출력한다.
				 
				tf_filename.setText(""); //
				lb_status.setText("파일전송중.....( 0 Byte)"); //
				BufferedOutputStream bout = new BufferedOutputStream(out, 2048); //
				DataInputStream din = new DataInputStream(sock.getInputStream()); //ct_sock클래스의 getInputStream 메소드를 받아온다.
				sendFile(bout, din, data, fileLength); //
				bout.close(); //
				din.close(); //
				
				lb_status.setText(file.getName() + "파일전송이 완료되었습니다."); //파일이름과 같이 파일전송이 완료되었습니다를 레이블에 출력한다.
				sock.close(); //소켓 연결 닫음
			}catch(IOException e1){ //
				System.out.println(e1); //
				lb_status.setText(address + "로의 연결에 실패하였습니다."); //
			}
		} else if(e.getSource() == bt_close){ //
			dispose(); //
		}
	}
	
	private void sendFile(BufferedOutputStream bout, DataInputStream din, byte[] data, int fileLength)
		throws IOException{
		int size = 2048; //
		int count = fileLength/size; //
		int rest = fileLength%size; //
		int flag = 1; //
		
		if(count == 0) flag = 0; //
		
		for(int i=0; i<=count; i++){ //
			if(i == count && flag == 0){ //
				bout.write(data, 0, rest); //
				bout.flush(); //
				return; //
			} else if(i == count){ //
				bout.write(data, i*size, rest); //
				bout.flush(); //
				return; //
			} else {
				bout.write(data, i*size, size); //
				bout.flush(); //
				lb_status.setText("파일전송중...(" + ((i+1)*size) + "/" + fileLength + " Byte)"); //
				din.readUTF(); //
			}
		}
	}

	class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we){ //
			System.exit(0); //
		}
	}
}
