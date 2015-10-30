package hhm.lucifer.activity;

import hhm.lucifer.datas.Tools;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity
{
   public void onCreate(Bundle savedInstanceState)
   {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.setting);
	  
	  //ȷ����ť
	  Button btnConfirm=(Button)this.findViewById(R.id.btnConfirm);
	  btnConfirm.setOnClickListener(new BtnConfirmListener());
	  //���ذ�ť
	  Button btnButton=(Button)this.findViewById(R.id.btnBack);
	  btnButton.setOnClickListener(new BtnBackListener());
	  
    }//end onCreate()
	 
   
	//*****��ťbtnConfirm�ļ����� BtnConfirmListener
	private class BtnConfirmListener implements View.OnClickListener
	{
		public void onClick(View v) 
		{
			//��ʾtxtExchange������
			EditText txtExchange=(EditText)findViewById(R.id.txtExchange);
			String exchange=txtExchange.getText().toString().trim();
			//���exchange����,�����
			if(Tools.rateIsDouble(exchange))
			{
				//Toast.makeText(getApplicationContext(), exchange, Toast.LENGTH_LONG).show();
				//�޸Ļ���,����true����false,�޸ĳɹ������¶�ȡ�����ļ�
				if(Tools.changeExchangeRate(exchange))					
					Toast.makeText(getApplicationContext(), "�����޸ĳɹ�", Toast.LENGTH_LONG).show();
				txtExchange.setText("");
			}
			else
			{
				txtExchange.setText("");
				Toast.makeText(getApplicationContext(), "������ʵ��", Toast.LENGTH_LONG).show();
			}
		}//end onClick()
	}//end class BtnConfirmListener
	 
	//*****��ťbtnBack�ļ�����BtnBackListener
	private class BtnBackListener implements View.OnClickListener
	{
		public void onClick(View v)
		{   
			Intent intent=new Intent();
			intent.setClass(SettingActivity.this, DiamoPriceActivity.class);
			startActivity(intent);
			finish();  //������ҳ��
		}
	}//end class BtnBackListener

}//end class SettingActivity
