package conn;


/**
* conn/ConnOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Conn.idl
* 2023年3月8日 星期三 下午03时12分20秒 CST
*/

public interface ConnOperations 
{
  String connect ();
  void sendMessage (String token, String message);
  String receiveMessage (String token);
  void createChatRoom (String token, String name);
  void listChatRooms (String token);
  void joinChatRoom (String token, String name);
  void leaveChatRoom (String token);
  void changeName (String token, String name);
} // interface ConnOperations
