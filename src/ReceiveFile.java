import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ReceiveFile extends Frame implements ActionListener
{
  public static final int port = 3777; //포트 지정
  public Label lbl; //레이블 변수
  public TextArea txt; //출력창 변수
  public Button btn; //버튼 변수
  
  public ReceiveFile(){ 
    super("파일전송"); //폼네임 지정
    setLayout(null); //레이아웃 매니저의 설정을 해제한다.
    lbl = new Label("파일 전송을 기다립니다."); //파일 전송을 기다립니다. 레이블 생성
    lbl.setBounds(10, 30, 230, 20); //레이블 위치 및 크기
    lbl.setBackground(Color.gray); //레이블 배경 색깔(회색) 설정
    lbl.setForeground(Color.white); //레이블 글짜 색깔(흰색) 설정
    add(lbl); //파일 전송을 기다립니다. 레이블 추가
    txt = new TextArea("", 0, 0, TextArea.SCROLLBARS_BOTH); //출력창의 오른쪽과 아래쪽에 스크롤바 생성
    txt.setBounds(10, 60, 230, 100); //출력창 위치 및 크기
    txt.setEditable(false); //TextArea에 있는 데이타들을 수정하지 못하도록
    add(txt); //출력창 추가.
    btn = new Button("닫기"); //닫기 버튼 생성
    btn.setBounds(105, 170, 40, 20); //닫기 버튼 위치 및 크기
    btn.setVisible(false); //닫기 버튼을 보이지 않게 설정
    btn.addActionListener(this); //버튼에 발생할 이벤트를 처리할 이벤트 리스너 객체를 연결한다.
    add(btn); //닫기 버튼 추가
    addWindowListener(new WinListener()); //
    setSize(250, 200); //
    show(); //윈도우를 보여준다
    
    try{
      ServerSocket socket = new ServerSocket(port); //지정된 포트로 서버소켓 생성 및 지정
      Socket sock = null; //Client용 소켓
      FileThread client = null; //
      try{
        sock = socket.accept(); //
        client = new FileThread(this, sock); //
        client.start(); //
      } catch(IOException e){
        System.out.println(e); //
        try{
          if(sock != null) sock.close(); //
        }catch(IOException e1){
          System.out.println(e1); //
        }finally{
          sock = null; //
        }
      }
    }catch(IOException e){}
  }
  
  public void actionPerformed(ActionEvent e){
    dispose(); //
  }
  
  class WinListener extends WindowAdapter
  {
    public void windowClosing(WindowEvent we){
      dispose(); //
    }
  }
}
