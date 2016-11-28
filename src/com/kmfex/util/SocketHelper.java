package com.kmfex.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
/**
 * @author liuxp
 *
 */
public class SocketHelper {
	
	public final int soLinger;
	
	public final int timeout;
	
	public final int sendBufferSize;
	
	public final int receiveBufferSize;
	
	public static final int RECEIVE_MAX_SIZE=1024*8;
	
	public final InetSocketAddress targetAddress;
	
	
	public SocketHelper(String ip,int port){
		targetAddress=new InetSocketAddress(ip,port);
		
		soLinger=1;
		timeout=60000;
		sendBufferSize=1024;
		receiveBufferSize=1024;
		
	}
	
	public SocketHelper(String ip,int port,int soLinger,int timeout,int sendBufferSize,int receiveBufferSize){
		targetAddress=new InetSocketAddress(ip,port);
		
		this.soLinger=soLinger;
		this.timeout=timeout;
		this.sendBufferSize=sendBufferSize;
		this.receiveBufferSize=receiveBufferSize;
	}
	
	
	
	
	public Socket getSocket(){
		Socket socket=SocketManager.getSocket();
		try {
			initSocket(socket);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
		
	}
	
	public Socket getSocket(int soLinger,int timeout,int sendBufferSize,int receiveBufferSize){
		Socket socket=SocketManager.getSocket();
		try {
			initSocket(socket,soLinger,timeout,sendBufferSize,receiveBufferSize);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
	}
	
	
	/**
	 * 执行socket通讯
	 * 
	 * @param send 发送信息
	 * @param receive 接收信息
	 * @return 读数据长度即receive中内容有效长度。-1 出错或者接收数据长度小于receive长度，0表示没内容可读，
	 */
	public int excuteSocket(byte[] send,byte[] receive){
		int re=-1;
		Socket socket=getSocket();
		DataOutputStream out=null;
		DataInputStream in=null;
		try {
			socket.connect(targetAddress);
			out = new DataOutputStream(socket.getOutputStream()); 
            in = new DataInputStream(socket.getInputStream()); 
           	out.write(send);
//           	out.flush();
			re=in.read(receive);
			System.out.println(re);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			if(in!=null)
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(!socket.isClosed())
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return re;
		
	}
	
	
	/**
	 * 执行Socket通讯
	 * 返回数组长度最大为 RECEIVE_MAX_SIZE
	 * @param send 发送信息
	 * @return 接收到的内容 socket出错则返回null
	 */
	public byte[] excuteSocket(byte[] send){
		Socket socket=getSocket();
		byte[] temp=new byte[receiveBufferSize<RECEIVE_MAX_SIZE?receiveBufferSize:RECEIVE_MAX_SIZE];
		
		DataOutputStream out=null;
		DataInputStream in=null;
		try {
			socket.connect(targetAddress);
			
			out = new DataOutputStream(socket.getOutputStream()); 
            in = new DataInputStream(socket.getInputStream()); 
            out.write(send);
       	out.flush();
			 int index=0,flag;
			 while(index<RECEIVE_MAX_SIZE){
				 if(temp.length-1>index){
					 flag=in.read(temp, index, 1);
					 if(in.available()<1||flag==-1){
						 temp=Arrays.copyOf(temp, index+1);
						 break;
					 }else{
						 index++;
					 }
				 }else{
					 temp=Arrays.copyOf(temp, temp.length+receiveBufferSize);
				 }
				 
			 }
			
			
		} catch (IOException e) {
			temp=null;
			e.printStackTrace();
		}finally{
			if(null!=in)
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(null!=out)
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(!socket.isClosed())
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return temp;
	}
	
	
	private void initSocket(Socket socket) throws SocketException{
		initSocket(socket,soLinger,timeout,sendBufferSize,receiveBufferSize);
	}

	private void initSocket(Socket socket,int soLinger,int timeout,int sendBufferSize,int receiveBufferSize) throws SocketException{
		socket.setSoLinger(true, soLinger);
		socket.setSoTimeout(timeout);
		socket.setSendBufferSize(sendBufferSize);
		socket.setReceiveBufferSize(receiveBufferSize);
		socket.setTcpNoDelay(true);
	}
	
	
	public static void main(String[] args){
		SocketHelper help=new SocketHelper("127.0.0.1",7777);
		byte[] t=new byte[10];
//		help.excuteSocket("kdjkfj".getBytes(),t );
		System.out.println(new String(help.excuteSocket("1187   00000000000000000000010501110800015687132800         1000000000000000    100000".getBytes())));
		

	}
}
