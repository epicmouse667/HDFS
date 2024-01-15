package api;


/**
* api/NameNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年12月28日 星期四 下午06时39分12秒 GMT+08:00
*/

public interface NameNodeOperations 
{

  //TODO: complete the interface design
  String open (String filepath, int mode);
  void close (String filepath);
} // interface NameNodeOperations
