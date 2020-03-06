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
        try {
            cppSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
