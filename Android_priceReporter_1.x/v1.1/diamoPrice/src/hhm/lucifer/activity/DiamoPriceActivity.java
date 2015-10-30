package hhm.lucifer.activity;

import java.util.ArrayList;
import java.util.List;

import hhm.lucifer.datas.CurrentPriceForms;
import hhm.lucifer.datas.Tools;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DiamoPriceActivity extends Activity {

	private CurrentPriceForms[] currentPriceForms = new CurrentPriceForms[19]; //��Ҫ������ݵ�18�ű�,������һ������
	private int formNum=0; //��������ļ۸�ѡ�����ı��
	private int colorIndex; //comboColor��ѡ����ֵ����Ӧ���±�
	private int netIndex; //comboNet��ѡ����ֵ����Ӧ���±�
	private Spinner spinnerColor; //��ɫ�����б�
	private Spinner spinnerNet;  //���������б�
	private TextView txtRMB;
    private TextView txtUS;
	private String[] paras = new String[3]; //��ʼ����Ҫ����������filePath sheetNum exchageRate
	
	public String[] getParas() {
		return paras;
	}

	public CurrentPriceForms[] getCurrentPriceForms() {
		return currentPriceForms;
	}
	
	//***************************���������ݳ�Ա
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     
        //��ȡ������ʾ���۵�TXT����
        txtRMB=(TextView)this.findViewById(R.id.txtRMB);
        txtUS=(TextView)this.findViewById(R.id.txtUS);
        
        //********************��ȡ��������
        Tools.getIniParameters(paras);
        //���ɲ���ʼ��18�ű�,����Tools��initial()
        if(Tools.initial(this))
        	Toast.makeText(getApplicationContext(), "��ʼ���ɹ�", Toast.LENGTH_LONG).show();
        else
        	Toast.makeText(getApplicationContext(), "��ʼ��ʧ��", Toast.LENGTH_LONG).show();
        	
        //********************���ʰ�ť
        Button btnExchange=(Button)this.findViewById(R.id.btnExchange);
        btnExchange.setOnClickListener(new BtnExchangeListener());
        //********************���ð�ť
        Button btnSet=(Button)this.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new BtnSetListener());
        //********************�˳���ť
        Button btnExit=(Button)this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new BtnExitListener());
        
        //����txtWeight
        EditText txtWeight=(EditText)this.findViewById(R.id.txtWeight);
        txtWeight.addTextChangedListener(new TxtWeightChangeListener(txtWeight));
        
        spinnerColor=(Spinner)this.findViewById(R.id.spinnerColor);
        spinnerNet=(Spinner)this.findViewById(R.id.spinnerNet);
        unloadSpinner(); //��ʼ������Spinner

    	
    	
        //Ϊ�����б����ø����¼�����Ӧ,�������Ӧ�����˵���ѡ��
        spinnerColor.setOnItemSelectedListener(new SpinnerColorListener());
        spinnerNet.setOnItemSelectedListener(new SpinnerNetListener());

//        txtRMB.setText("11111");
//		txtUS.setText("22222");

    }//end onCreate()
    
   
    /***
     * ����۸�
     */
    public void calculation() {
		double rmbPrice = 0;
		double dollarPrice = 0;
		//�õ���ı��
		int nub =formNum;
		//�õ���ѡ����ɫ�;����±�
		int _colorIndex = colorIndex;
		int _netIndex = netIndex;
		//�õ�����
		double exchangeRate = Double.parseDouble(paras[2]);

		if (_netIndex >= 0 && _colorIndex >= 0) {
			if (nub <= 6)
				dollarPrice = this.getCurrentPriceForms()[nub].getLight_PriceForm()[_colorIndex][_netIndex]*100;
			else
				dollarPrice = this.getCurrentPriceForms()[nub].getWeight_PriceForm()[_colorIndex][_netIndex]*100;
		}

		//��ʾ
		rmbPrice = exchangeRate * dollarPrice;
		txtRMB.setText(Tools.getCurrencyFormat(rmbPrice, "china"));
		txtUS.setText(Tools.getCurrencyFormat(dollarPrice,"america"));
	}
    

    
    /***
     * ���Ҳ����Ա�����ʼ���ص�ʱ�������б����ʾ
     */
    public void unloadSpinner()
    {
    	List<String> list=new ArrayList<String>();
    	list.add("---NULL---");
    	ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinnerColor.setAdapter(adapter);
    	spinnerNet.setAdapter(adapter);
    	
    	txtRMB.setText("��0.00");
		txtUS.setText("��0.00");
    }
    
    /***
     * ����ʯ�������ı�ż������������б�
     */
	public void loadSpinner() {
		
		//��ɫ
		String lightColor[] = { "D-F", "G-H", "I-J", "K-L", "M-N" };
		String weightColor[] = { "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M" };
		//����
		String lightNet[] = { "IF-VVS", "VS", "SI1", "SI2", "SI3", "P1", "P2",
				"P3" };
		String weightNet[] = { "IF", "VVS1", "VVS2", "VS1", "VS2", "SI1",
				"SI2", "SI3", "P1", "P2", "P3" };
		
	   List<String> colorList=new ArrayList<String>();
	   List<String> netList=new ArrayList<String>();
       
	   if(formNum<=6)  //�����ǰ6�ű�
	   {
		   for(int i1=0;i1<lightColor.length;i1++)
			   colorList.add(lightColor[i1]);
		   for(int j1=0;j1<lightNet.length;j1++)
			   netList.add(lightNet[j1]);
	   }
	   else
	   {
		   for(int i1=0;i1<weightColor.length;i1++)
			   colorList.add(weightColor[i1]);
		   for(int j1=0;j1<weightNet.length;j1++)
			   netList.add(weightNet[j1]);
	   }
	   //��������������
	   ArrayAdapter<String> colorAdapter,netAdapter;
	   colorAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,colorList);
	   netAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,netList);
	   //�����б���ʽ
	   colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   netAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   //����������ӵ������б�
	   spinnerColor.setAdapter(colorAdapter);
	   spinnerNet.setAdapter(netAdapter);
	}
    
    //************************�������ڲ���
    //**********************���ʰ�ť�ļ�����
    private class BtnExchangeListener implements View.OnClickListener
    {
		public void onClick(View v)
		{
			String exchange="��ǰ���ʣ�"+paras[2];
//			exchange+="\n��ǰ����"+paras[1];
//			exchange+="\nxls·����"+paras[0];
			Toast.makeText(getApplicationContext(), exchange, Toast.LENGTH_LONG).show();
		}
    }//end class BtnExchangeListener
	
    //************************���ð�ť�ļ�����
    private class BtnSetListener implements View.OnClickListener
    {
		public void onClick(View v)
		{
			//Toast.makeText(getApplicationContext(), "���ð�ť", Toast.LENGTH_LONG).show();
			Intent intent=new Intent();
			intent.setClass(DiamoPriceActivity.this, SettingActivity.class);
			//��ת��������ҳ��
			startActivity(intent);
			finish();
		}
    	
    }//end class BtnSetListener
    
    //************************�˳���ť�ļ�����
    private class BtnExitListener implements View.OnClickListener
    {
		public void onClick(View v)
		{
			//Toast.makeText(getApplicationContext(), "�˳���ť", Toast.LENGTH_LONG).show();
			finish();
		}
    }//end class BtnExitListener
   
    //************************txtWeight������
    private class TxtWeightChangeListener implements TextWatcher
    {
    	EditText txtWeight;  //��������������������
    	public TxtWeightChangeListener(EditText et)
    	{
    	   txtWeight=et;	
    	}
    	
		public void afterTextChanged(Editable s) {
			
			String weight=txtWeight.getText().toString().trim();
			//��������weight�Ϸ�����õ���Ӧ�ı���
			if(Tools.checkWeight(weight))
			{
				double value=Double.parseDouble(weight);
				formNum=Tools.getFormNumByWeight(value);
				loadSpinner();
				//Toast.makeText(getApplicationContext(), weight+",��"+formNum+"�ű�", Toast.LENGTH_LONG).show();
			}else{
				unloadSpinner();
				formNum=0;
			}
				
		}

		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	
		}

		public void onTextChanged(CharSequence s, int start, int before,int count) {	
			
		}
    	
    }//end class TxtWeightChangeListener
    
    //******************��ɫ�����б�spinnerColor�ļ�����
    private class SpinnerColorListener implements OnItemSelectedListener
    {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{
			colorIndex=arg2;
			if(formNum!=0) //�����18�ű��е�һ��
			{
				calculation(); //����
				//Toast.makeText(getApplicationContext(), colorIndex+"", Toast.LENGTH_LONG).show();
				arg0.setVisibility(View.VISIBLE);
			}else
				;//Toast.makeText(getApplicationContext(), colorIndex+",formNum=0", Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView<?> arg0) 
		{
			
		}
    }//end class SpinnerColorListener
    
  //******************���������б�spinnerNet�ļ�����
    private class SpinnerNetListener implements OnItemSelectedListener
    {
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3)
	{
		netIndex=arg2;
		if(formNum!=0) //�����18�ű��е�һ��
		{
			calculation(); //����
			//Toast.makeText(getApplicationContext(), netIndex+"", Toast.LENGTH_LONG).show();
			arg0.setVisibility(View.VISIBLE);
		}else
			;//Toast.makeText(getApplicationContext(), netIndex+",formNum=0", Toast.LENGTH_LONG).show();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
			
	}

    }//end class SpinnerNetListener
    
}//end class DiamoPrice