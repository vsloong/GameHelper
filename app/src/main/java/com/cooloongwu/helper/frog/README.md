# 旅行青蛙修改器

部分代码来自[aa65535/TabikaeruArchiveModifier](https://github.com/aa65535/TabikaeruArchiveModifier)，感谢原作者。

### 使用前请注意
1. 确定安装的是正版“旅行青蛙”，目前支持的是1.0.4版本的。
2. 使用修改器修改之前，请后台杀掉“旅行青蛙”程序
3. 三叶草最大支持输入9个9，抽奖券最大支持输入3个9
4. 明信片导出位置在你手机存储的 DCIM 下“旅行青蛙明信片”文件夹内  
  
##### ====================分隔线====================
## 程序员知识点提升请看这里
#### 1、RandomAccessFile
RandomAccessFile类的主要功能是提供随机读取、修改功能，可以读取、修改指定位置的内容。  

因为这个游戏只是一个单机游戏，所以游戏数据都在本地存档，我们使用这个类来修改存档就可以改变数据了。

```java
    RandomAccessFile randomAccessFile = new RandomAccessFile(File file, String mode);  
```

第一个参数指定一个文件，第二个参数指定操作模式，操作模式有以下四种：  

1."r"    以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。  
  
2."rw"   打开以便读取和写入。  

3."rws"  打开以便读取和写入。相对于 "rw"，"rws" 还要求对“文件的内容”或“元数据”的每个更新都同步写入到基础存储设备。  
  
4."rwd"  打开以便读取和写入，相对于 "rw"，"rwd" 还要求对“文件的内容”的每个更新都同步写入到基础存储设备。  

```java
randomAccessFile.read(byte[] b);//将文件内同读取到byte数组中
randomAccessFile.seek(long pos);//设置指针的位置
randomAccessFile.skipBytes(int n);//设置指针跳过多少个字节
randomAccessFile.readByte();//读取文件一个字节
randomAccessFile.readInt();//读取文件中整形数据
randomAccessFile.writeBytes(String s);//将字符串写入到文件中去
randomAccessFile.writeInt(int v);//将整形数据写入到文件中去
randomAccessFile.close();//关闭操作
```
