package hhm.lucifer.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter.ViewBinder;

public class DiamoPriceActivity extends Activity {

	private CurrentPriceForms[] currentPriceForms = new CurrentPriceForms[19]; //��Ҫ������ݵ�18�ű�,������һ������
	private int formNum=0; //��������ļ۸�ѡ�����ı��
	private int colorIndex; //comboColor��ѡ����ֵ����Ӧ���±�
	private int netIndex; //comboNet��ѡ����ֵ����Ӧ���±�
	private Spinner spinnerColor; //��ɫ�����б�
	private Spinner spinnerNet;  //���������б�
	private EditText txtWeight;  //����������EditText
	private EditText txtUnitPrice; //��ʾ���۵�EditText
	private EditText txtDiscount;  //��ʾ�ۿ۵�EditText
	private EditText txtTotalPrice; //��ʾ�ܼ۵�EditText
	private RadioGroup radioGroup; //��ѡ��ť��
	private RadioButton rdbUS;  //����Ԫ��ʾ�ĵ�ѡ��ť
	private RadioButton rdbRMB;  //���������ʾ�ĵ�ѡ��ť
	
	private double weightEnter; //�������ʯ����
	private double discountEnter; //������ۿ�
	private double unitPriceEnter;  //����ĵ���
	private double totalPriceEnter;  //������ܼ�
	private double US_unitPrice; //��Ԫ����
	private double US_totalPrice;  //��Ԫ�ܼ�
	private double RMB_unitPrice; //����ҵ���
	private double RMB_totalPrice; //������ܼ�
	private int discount; //�ۿ�
	
	private DecimalFormat df=new DecimalFormat("###");  //��ʽ����ʾ�Ķ���
	
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
         
        //********************��ȡ��������
        Tools.getIniParameters(paras);
        //���ɲ���ʼ��18�ű�,����Tools��initial()
        if(Tools.initial(this))
        	Toast.makeText(getApplicationContext(), "��ʼ���ɹ�", Toast.LENGTH_LONG).show();
        else
        	Toast.makeText(getApplicationContext(), "��ʼ��ʧ��", Toast.LENGTH_LONG).show();
        
        txtUnitPrice=(EditText)this.findViewById(R.id.txtUnitPrice); //�õ�txtUnitPrice�ؼ�
        txtUnitPrice.addTextChangedListener(new TxtUnitPriceListener()); //��Ӽ����¼�
        txtUnitPrice.setOnFocusChangeListener(new TxtFocusChangeLinster()); //��������ı�
        
        txtDiscount=(EditText)this.findViewById(R.id.txtDiscount);
        txtDiscount.addTextChangedListener(new TxtDiscountListener());  //����¼�
        txtDiscount.setOnFocusChangeListener(new TxtFocusChangeLinster());
        
        txtTotalPrice=(EditText)this.findViewById(R.id.txtTotalPrice);
        txtTotalPrice.addTextChangedListener(new TxtTotalPriceListener()); //����¼�
        txtTotalPrice.setOnFocusChangeListener(new TxtFocusChangeLinster());
        
        setTxtDisable();
        
        //*******************��ѡ��ť��
        radioGroup=(RadioGroup)this.findViewById(R.id.radiogroup1);
        radioGroup.setOnCheckedChangeListener(new RdbChangedListener());
        //******************���������ʾ�ĵ�ѡ��ť
        rdbRMB=(RadioButton)this.findViewById(R.id.rdbRMB);
        //******************����Ԫ��ʾ�ĵ�ѡ��ť
        rdbUS=(RadioButton)this.findViewById(R.id.rdbUS);
        //********************���ʰ�ť
        Button btnExchange=(Button)this.findViewById(R.id.btnExchange);
        btnExchange.setOnClickListener(new BtnExchangeListener());
        //********************���ð�ť
        Button btnSet=(Button)this.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new BtnSetListener());
        //********************ˢ�°�ť
        Button btnExit=(Button)this.findViewById(R.id.btnRefresh);
        btnExit.setOnClickListener(new BtnRefresListener());
        
        //����txtWeight
        txtWeight=(EditText)this.findViewById(R.id.txtWeight);
        txtWeight.addTextChangedListener(new TxtWeightChangeListener());
        
        spinnerColor=(Spinner)this.findViewById(R.id.spinnerColor);
        spinnerNet=(Spinner)this.findViewById(R.id.spinnerNet);
        unloadSpinner(); //��ʼ������Spinner

        //Ϊ�����б����ø����¼�����Ӧ,�������Ӧ�����˵���ѡ��
        spinnerColor.setOnItemSelectedListener(new SpinnerColorListener());
        spinnerNet.setOnItemSelectedListener(new SpinnerNetListener());

    }//end onCreate()
    
   
    private void setTxtDisable()
    {
    	txtUnitPrice.setEnabled(false);
    	txtDiscount.setEnabled(false);
    	txtTotalPrice.setEnabled(false);
    }
    
    private void setTxtEnable()
    {
    	txtUnitPrice.setEnabled(true);
    	txtDiscount.setEnabled(true);
    	txtTotalPrice.setEnabled(true);
    }
    
    /***
     * �������ѡ��ťѡ����ȷ������۸�,��ˢ�°�ť��Ҳ�������������
     */
    public void calculation() {
    	
    	setTxtEnable(); //������������editText
    	discount=100;  //ÿ�γ�ʼ���ۿ�Ϊ100
		int nub =formNum; //�õ���ı��
		//�õ���ѡ����ɫ�;����±�
		int _colorIndex = colorIndex;
		int _netIndex = netIndex;
		//�õ�����
		double exchangeRate = Double.parseDouble(paras[2]);

		if (_netIndex >=0 && _colorIndex >= 0) {
			if (nub <= 6)
				US_unitPrice = this.getCurrentPriceForms()[nub].getLight_PriceForm()[_colorIndex][_netIndex]*100;
			else
				US_unitPrice = this.getCurrentPriceForms()[nub].getWeight_PriceForm()[_colorIndex][_netIndex]*100;
		}

		//��ʾ
		RMB_unitPrice = exchangeRate * US_unitPrice;
		RMB_totalPrice=RMB_unitPrice*weightEnter;
		US_totalPrice=US_unitPrice*weightEnter;
		
		if(rdbUS.isChecked())  //����Ԫ��ʾ
		{
			unitPriceEnter=US_unitPrice;
			totalPriceEnter=US_totalPrice;
			showPrice(df.format(US_unitPrice),df.format(discount),df.format(US_totalPrice));
		}else  //���������ʾ
		{
			unitPriceEnter=RMB_unitPrice;
			totalPriceEnter=RMB_totalPrice;
			showPrice(df.format(RMB_unitPrice),df.format(discount),df.format(RMB_totalPrice));
		}
//		txtRMB.setText(Tools.getCurrencyFormat(rmbPrice, "china"));
//		txtUS.setText(Tools.getCurrencyFormat(dollarPrice,"america"));
	}
    

    /****
     * ��ʾ���ۣ��ܼۺ��ۿ�
     * @param unitPrice ����
     * @param discount �ۿ�
     * @param totalPrice  �ܼ�
     */
    public void showPrice(String unitPrice,String discount,String totalPrice)
    {
    	txtUnitPrice.setText(unitPrice);
		txtDiscount.setText(discount);
		txtTotalPrice.setText(totalPrice);
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
    	
//    	txtRMB.setText("��0.00");
//		txtUS.setText("��0.00");
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
	
	//*****************���������Ƿ�ı����
	private class TxtFocusChangeLinster implements View.OnFocusChangeListener
	{
		public void onFocusChange(View v, boolean hasFocus) {
			EditText tmp=(EditText)v;
        	tmp.selectAll();
		}
	}
	
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
    
    //************************ˢ�°�ť�ļ�����
    private class BtnRefresListener implements View.OnClickListener
    {
		public void onClick(View v)
		{
			//Toast.makeText(getApplicationContext(), "�˳���ť", Toast.LENGTH_LONG).show();
		    if(formNum!=0) //����ֵ��Ч������²�����ˢ��
			    calculation();
		}
    }//end class BtnExitListener
   
    //************************txtWeight������
    private class TxtWeightChangeListener implements TextWatcher
    {
		public void afterTextChanged(Editable s) {
			
			String weight=txtWeight.getText().toString().trim();
			//��������weight�Ϸ�����õ���Ӧ�ı���
			if(Tools.checkWeight(weight))
			{
				weightEnter=Double.parseDouble(weight);  //�õ����������
				formNum=Tools.getFormNumByWeight(weightEnter);
				loadSpinner();
				//Toast.makeText(getApplicationContext(), weight+",��"+formNum+"�ű�", Toast.LENGTH_LONG).show();
			}else{
				//�ÿ�
				unloadSpinner();
				setTxtDisable();
				showPrice("","","");
				formNum=0;
			}
				
		}

		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	
		}

		public void onTextChanged(CharSequence s, int start, int before,int count) {	
			
		}
    	
    }//end class TxtWeightChangeListener
    
    //**********************���������txtUnitPrice������
    private class TxtUnitPriceListener implements TextWatcher
    {
		public void afterTextChanged(Editable s) 
		{
			if(txtUnitPrice.isFocused()) //����Լ��ǽ��㣬��ִ��
			{
				String price=txtUnitPrice.getText().toString();
				if(price.equals(""))
					   unitPriceEnter=0;
				else
					   unitPriceEnter=Double.parseDouble(price);
				
				if(rdbUS.isChecked()) //�������Ԫ��ʾ
				{
					discountEnter=(int)(unitPriceEnter*100/US_unitPrice);
					totalPriceEnter=unitPriceEnter*weightEnter;
					//showPrice(df.format(unitPriceEnter),df.format(discountEnter) , df.format(totalPriceEnter));	
					txtDiscount.setText(df.format(discountEnter));  //�ۿ���ʾ��Ӧ�仯
					txtTotalPrice.setText(df.format(totalPriceEnter));   //�ܼ���ʾ��Ӧ�仯
				}else{ //���������ʾ
					
					discountEnter=(int)(unitPriceEnter*100/RMB_unitPrice);
					//Toast.makeText(getApplicationContext(), discountEnter+"", Toast.LENGTH_LONG).show();
					totalPriceEnter=unitPriceEnter*weightEnter;
					txtDiscount.setText(df.format(discountEnter));  //�ۿ���ʾ��Ӧ�仯
					txtTotalPrice.setText(df.format(totalPriceEnter));   //�ܼ���ʾ��Ӧ�仯
				}
			}//end if focused
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
    	
    }// end class TxtUnitPriceListener
    
    //**********************�ۿ������txtDiscount������
    private class TxtDiscountListener implements TextWatcher
    {

		public void afterTextChanged(Editable s) {
			if(txtDiscount.isFocused())  //�ǽ����ִ��
			{
				String _discount=txtDiscount.getText().toString();
				if(_discount.equals(""))
					   discountEnter=0;
				else
					   discountEnter=Double.parseDouble(_discount);
				
				if(rdbUS.isChecked()) //�������Ԫ��ʾ
				{
					unitPriceEnter=US_unitPrice*discountEnter/100;
					totalPriceEnter=unitPriceEnter*weightEnter;
					//showPrice(df.format(unitPriceEnter),df.format(discountEnter) , df.format(totalPriceEnter));	
					txtUnitPrice.setText(df.format(unitPriceEnter));  //������ʾ��Ӧ�仯
					txtTotalPrice.setText(df.format(totalPriceEnter));   //�ܼ���ʾ��Ӧ�仯
				}else{ //���������ʾ
					unitPriceEnter=RMB_unitPrice*discountEnter/100;
					totalPriceEnter=unitPriceEnter*weightEnter;
					//showPrice(df.format(unitPriceEnter),df.format(discountEnter) , df.format(totalPriceEnter));	
					txtUnitPrice.setText(df.format(unitPriceEnter));  //������ʾ��Ӧ�仯
					txtTotalPrice.setText(df.format(totalPriceEnter));   //�ܼ���ʾ��Ӧ�仯
				}
			}// end is  isFocused					
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		 
		}
    	
    }//end class TxtDiscount
    
    //**********************�ܼ������txtPrice������
    private class TxtTotalPriceListener implements TextWatcher
    {
		public void afterTextChanged(Editable s) {
			if(txtTotalPrice.isFocused())  //�ǽ����ִ��
			{
				String total=txtTotalPrice.getText().toString();
				if(total.equals(""))
					   totalPriceEnter=0;
				else
					totalPriceEnter=Double.parseDouble(total);
				
				if(rdbUS.isChecked()) //�������Ԫ��ʾ
				{
					unitPriceEnter=totalPriceEnter/weightEnter;
					discountEnter=unitPriceEnter*100/US_unitPrice;
					//showPrice(df.format(unitPriceEnter),df.format(discountEnter) , df.format(totalPriceEnter));
					txtDiscount.setText(df.format(discountEnter));
					txtUnitPrice.setText(df.format(unitPriceEnter));  //������ʾ��Ӧ�仯
				}else{ //���������ʾ
					unitPriceEnter=totalPriceEnter/weightEnter;
					discountEnter=unitPriceEnter*100/RMB_unitPrice;
					//showPrice(df.format(unitPriceEnter),df.format(discountEnter) , df.format(totalPriceEnter));
					txtDiscount.setText(df.format(discountEnter));
					txtUnitPrice.setText(df.format(unitPriceEnter));  //������ʾ��Ӧ�仯
				}
			}// end is  isFocused	
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
    	
    }
    
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
			;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
			
	}

    }//end class SpinnerNetListener
    
    //******************��ѡ��ť��radioGroup1�ļ�����
    private class RdbChangedListener implements OnCheckedChangeListener
    {
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			if(formNum!=0)  
			{
				double exchangeRate = Double.parseDouble(paras[2]);
				if(checkedId==rdbUS.getId())  //�����ѡ����Ԫ
				{
				   //����ʾ������һ�����Ԫ
					unitPriceEnter=unitPriceEnter/exchangeRate;
					totalPriceEnter=totalPriceEnter/exchangeRate;
					txtUnitPrice.setText(df.format(unitPriceEnter));
					txtTotalPrice.setText(df.format(totalPriceEnter));
				   //Toast.makeText(getApplicationContext(), "��Ԫ", Toast.LENGTH_LONG).show();	
				}else{
					unitPriceEnter=unitPriceEnter*exchangeRate;
					totalPriceEnter=totalPriceEnter*exchangeRate;
					txtUnitPrice.setText(df.format(unitPriceEnter));
					txtTotalPrice.setText(df.format(totalPriceEnter));
					//Toast.makeText(getApplicationContext(), "�����", Toast.LENGTH_LONG).show();
				}
			}
			else
				setTxtDisable();
		}
    	
    }//end class RdbChangedListener
    
}//end class DiamoPrice