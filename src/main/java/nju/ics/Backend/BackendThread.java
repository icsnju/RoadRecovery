package nju.ics.Backend;

import nju.ics.Main.PathRestoration;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class BackendThread extends Thread {

    protected PrintStream out;
    protected String JSONData;
    protected Socket cppSocket;
    protected int defaultByteSize = 10_000;

    public BackendThread(Socket cppSocket) {
        //init fields
        this.cppSocket = cppSocket;
        try {
            //接受数据，但不允许有中文，因为会乱码
            DataInputStream in = new DataInputStream(cppSocket.getInputStream());
            byte[] buffer = new byte[defaultByteSize];  //缓冲区的大小
            in.read(buffer);               //处理接收到的报文，转换成字符串
            JSONData = new String(buffer).trim();
            System.out.println(JSONData);

            // 获得输出输出流
            out = new PrintStream(cppSocket.getOutputStream());
        } catch (IOException e) {
            try {
                cppSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        PathRestoration pathRestoration = new PathRestoration();
        String returnedJSONData = pathRestoration.pathRestorationMethod(JSONData);
        byte[] returnedBytes = returnedJSONData.getBytes();
        out.write(returnedBytes, 0, returnedBytes.length);
//        String returned = "I am server, and receive your request.";
//        out.write(returned.getBytes(), 0, returned.getBytes().length);
        try {
            cppSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
