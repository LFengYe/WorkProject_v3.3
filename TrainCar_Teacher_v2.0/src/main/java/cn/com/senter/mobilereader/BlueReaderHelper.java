package cn.com.senter.mobilereader;

import android.content.Context;
import android.os.Handler;

import cn.com.senter.mediator.BluetoothReader;


public class BlueReaderHelper
{
	private Context context;
	
	private BluetoothReader bluecardreader;
	
	public BlueReaderHelper(Context context, Handler handler)
	{
		this.context=context;
		bluecardreader = new BluetoothReader(handler, context);
	}	
	
	public String read()
	{       
		return bluecardreader.readCard_Sync();
	}
	
	public boolean registerBlueCard(String address)
	{
		return bluecardreader.registerBlueCard(address);
	}

	public int readSimICCID(byte [] iccid){
		return bluecardreader.readSimICCID(iccid);
	}
	
	public boolean writeSimCard(String imsi, String smsNo){
		return bluecardreader.writeSimCard(imsi, smsNo);
	} 	
	
	public void setServerAddress(String server_address) {
		bluecardreader.setServerAddress(server_address);
	}

	public void setServerPort(int server_port) {
		bluecardreader.setServerPort(server_port);
	} 
	
	public String  TransmitCard(String mAPDU){
		return bluecardreader.TransmitCard(mAPDU);
	}
	
	public boolean unRegisterBlueCard(){
		return bluecardreader.unRegisterBlueCard();
	}
	
	public boolean SimInit(){
		return bluecardreader.SimInit();
	}
}
