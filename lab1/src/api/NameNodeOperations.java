package api;


/**
* api/NameNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��12��28�� ������ ����06ʱ39��12�� GMT+08:00
*/

public interface NameNodeOperations 
{

  //TODO: complete the interface design
  String open (String filepath, int mode);
  void close (String filepath);
} // interface NameNodeOperations
