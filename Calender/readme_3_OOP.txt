version_3
�汾3(�����v2):
�ϴ��޸�ʱ��:
1."�ϸ���"���ں�"�¸���"������ǳɫ��ʾ��������
  cld_lastMon|cld_nextMon
2.���ڱ��£�ʹ��cld_curMon,���ڵ�ǰ������ʹ��cld_today
3.����CldDate��
  m_year|m_month|m_day|m_week
4.ע�⣺prototype�����ǿ���������ǰ��

OOP
Լ��
1.1900-01-01Ϊ����һ
2.week��ʾ����|day��ʾ����
3.���е���ʽǰ׺Ϊ"cld_"
4.����jq.1.7.2
5.���ʱ��С��19000101���Ϊ19000101
6.����һ��������Ϊ1--6��������Ϊ0

����
1.�������դ������
  1.����Ǳ��µ�ʱ��,�򵯳��Ի�����ʾ�����������
  2.��������������ʱ��,��ô������ǰ��ʾһ����
  3.��������������ʱ��,��ô���������ʾһ����

��Class-CldDate��
���캯��:
CldDate(_dateStr, _splitter="")
���ݳ�Ա��
this.m_dateStr		//�����������ַ���
this.m_splitter		//�����շָ���
this.m_year			//i-���
this.m_month		//i-�·�
this.m_day			//i-����
this.m_week		//i-����
������:
CldDate.prototype.leapYrMonths[]
CldDate.prototype.nonLeapYrMonths[]
�෽��:
CldDate.toString()
CldDate.prototype.isLeapYear(_year)
CldDate.prototype.paddingZero(_month)
CldDate.prototype.getFirstDayDate()
CldDate.prototype.getLastDayDate()

CldDate.prototype.addDay()
CldDate.prototype.minusDay()
Calendar.prototype.addMonth()
Calendar.prototype.minusMonth()
Calendar.prototype.addYear()
Calendar.prototype.minusYear()

CldDate.prototype._diffDays()
CldDate.prototype._calculateWeek(int _diffDay)

��Class-Calender��
���캯��:
Calendar(char _splitter="", obj _styleOpts=null, int _cldRows=5, int _cldCols=7)

���ݳ�Ա:
Calendar.m_cldRows
Calendar.m_cldCols
Calendar.m_styleOpts		//������ʽ�����ļ�

Calendar.m_toDate		//��������,���ڻָ���m_curDate
Calendar.m_curDate		//��ǰ����
Calendar.m_curCalendarDatas	//����Ŀǰ��ʹ�õ���������(��ά����)
ע�⣺����ʹ��Calendar.m_toDate=Calendar.m_curDate=XXX
�����ָ��ͬһ������

�෽��:
Calender.prototype.initCalendar(char _splitter)
Calender.prototype.toString()

Calender.prototype.buildCalendarData_en(CldDate _cldDate=null)
Calendar.prototype.buildCalendarData_cn(CldDate _cldDate=null)
Calendar.prototype.buildCalendarGrid_en(_lang='CN')
Calendar.prototype.buildCalendarGrid_cn(_lang='CN')
    
Calendar.prototype.displayYrMonth($_jqObj, _CldDate=null)

Calendar.prototype.addMonth()      //�ı�m_curDate,��m_curCalendarDatas
Calendar.prototype.minusMonth()
Calendar.prototype.addYear()
Calendar.prototype.minusYear()
Calendar.prototype.restoreDate()       //�ָ�m_curDate��m_toDate,��m_curCalendarDatas
Calendar.prototype.getCertainDateObj()	
Calendar.prototype.getFirstDateObj()   //��m_curCalendarDatas��ȡ		
Calendar.prototype.getLastDateObj()    //ͬ��

��view�㡿
#�ļ���calendar_3_view.html
#���е�CSS-class|CSS-id�����ò�����ʶǰ׺"cld_"
   ����HTML�ؼ�CSS-class|CSS-id�������"cld_"�Լ�"�ؼ���ʶǰ׺"(�簴ťΪ"btn_")
   ���ʶǰ׺��ǰ���ؼ���ʶǰ׺�ں���cld_btn_back (����)

#���ýṹ
<div class="cld_container"></div>

#���������ṹ
<label>������ʽ</label>
	<select class="selc_cldFormatType" autocomplete="off">
		<option value="CN" selected="selected">��ʽ</option>
		<option value="EN">��ʽ</option>
	</select>
	<label>��������</label>
	<select class="selc_cldTitleType" autocomplete="off">
		<option value="CN" selected="selected">����</option>
		<option value="EN">Ӣ��</option>
		<option value="NUM">����</option>
	</select>
	<br /><br />
	
	<div class="cld_container">
			<div class="cld_header">
					<input type="button" class="cld_btn_lastYear" value="<<"/>
					<input type="button" class="cld_btn_lastMonth" value="<"/>
					<span class="cld_span_yrMonth">2014��9��</span>
					<input type="button" class="cld_btn_nextMonth" value=">"/>
					<input type="button" class="cld_btn_nextYear" value=">>"/>
					<input type="button" class="cld_btn_restore" value="��ԭ" />
			</div>
			<div class="cld_body"></div>
	</div>
#���HTML�ؼ�
.cld_selc_formatType
.cld_selc_titleType

.cld_btn_lastYear
.cld_btn_lastMonth
.cld_btn_nextMonth
.cld_btn_nextYear
.cld_btn_restore

.cld_span_yrMonth

.cld_body (div)

��CSS�ļ���
#�ļ���:calendar_3.css
#����ʽ��صĺ���
Calendar::buildCalendarGrid_en()  undone  //ע�����º����µ��ж� �·ݲ�Ҫ �����������(-1 1 11 -11)
Calendar::buildCalendarGrid_cn()  done
#�����ʽ��
����դ����:cld_calendarGrid
����դ��title:cld_calendaGrid th

�ϸ������ڸ�����ʽ:cld_lastMthStyle
�������ڸ�����ʽ:cld_curMthStyle
�¸�������������ɫ:cld_nextMthStyle
��껮��ʱ��ʽ:cld_mouseover
��Ϣ����ʽ:cld_weekend






    












