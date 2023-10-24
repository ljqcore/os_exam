package os;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;


public class GUI extends JFrame{
    // 存放进程容器
    private ArrayList<PCB> reserveList = new ArrayList<PCB>();
    private ArrayList<PCB> readyList = new ArrayList<PCB>();
    private ArrayList<PCB> hookList = new ArrayList<PCB>();  // 阻塞
    private ArrayList<PCB> endList = new ArrayList<PCB>();   // 终止
    private PCB[] cpuList = new PCB[2];
    // 系统时间
    private int time = 0;
    // 时间片大小:5
    private int singleTime = 5;
    // 最大道数:4
    private int channel = 4;
    // 存放内存信息
    private final int memorySize=256;
    private final int osSize=32;
    private int[] memoryList = new int[memorySize];

    // 系统时间的控件
    private JPanel timePanel;
    private JLabel timeJlb = new JLabel(Integer.toString(time));

    // 创建进程的相关控件
    private JPanel createPanel;
    private JTextField pidField, runtimeField, memoryField, priorityField, predecessorField;
    private JLabel pidLabel,runtimeLabel,memoryLabel,priorityLabel,predecessorLabel;


    // CPU使用情况的控件
    private JPanel cpuPanel;
    private JLabel cpu1Label,cpu2Label;
    private JTextField cpu1TextField,cpu2TextField;
    private ProgressBar progressBar1,progressBar2;
    private int cpuPid = -1; // 存储正在编辑的 JTextField 的引用
    private int cpu = 0;

    //后备队列界面
    private JPanel reservePanel;
    private JLabel reserveLabel;
    private Table reserveTable;

    //就绪队列界面
    private JPanel readyPanel;
    private JLabel readyLabel;
    private Table readyTable;


    //阻塞队列界面
    private JPanel hungPanel;
    private JLabel hungLabel;
    private Table hungTable;

    //完成队列界面
    private JPanel endPanel;
    private JLabel endLabel;
    private Table endTable;

    // 内存空间区的控件
    private  MemoryPanel mempl;

    // 按钮组件
    private JPanel buttonPanel;
    private JButton createBtn,runBtn,closeBtn,hookBtn,unhookBtn;

    /** 初始化界面 **/
    public GUI() {
        // 设计总体界面
        this.setLayout(null);
        this.setBounds(30,30,1500,870);
        this.setDefaultCloseOperation(3);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // 系统时间的控件
        timePanel=new JPanel();
        timePanel.setBounds(20,10,350,70);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("系统时间");
        Font font2 = new Font("宋体", Font.BOLD, 18);
        titledBorder2.setTitleFont(font2);
        timePanel.setBorder(titledBorder2);

        timeJlb = new JLabel(Integer.toString(time));
        timeJlb.setBounds(10,10,100,50);
        timeJlb.setFont(new Font("宋体", Font.BOLD, 20));
        timePanel.add(timeJlb);

        this.add(timePanel);

        //创建进程界面
        createPanel=new JPanel(null);
        createPanel.setBounds(20,110,350,300);//总体(1600,900)
        TitledBorder titledBorder0 = BorderFactory.createTitledBorder("创建进程");
        Font font0 = new Font("宋体", Font.BOLD, 18);
        titledBorder0.setTitleFont(font0);
        createPanel.setBorder(titledBorder0);

        pidLabel = new JLabel("进程PID:");
        pidLabel.setBounds(30, 40, 100, 30);
        pidLabel.setFont(new Font("宋体",Font.BOLD,20));
        createPanel.add(pidLabel);
        runtimeLabel = new JLabel("运行时间:");
        runtimeLabel.setBounds(30, 90, 100, 30);
        runtimeLabel.setFont(new Font("宋体",Font.BOLD,20));
        createPanel.add(runtimeLabel);
        memoryLabel = new JLabel("内存大小:");
        memoryLabel.setBounds(30, 140, 100, 30);
        memoryLabel.setFont(new Font("宋体",Font.BOLD,20));
        createPanel.add(memoryLabel);
        priorityLabel = new JLabel("优先权:");
        priorityLabel.setBounds(30, 190, 100, 30);
        priorityLabel.setFont(new Font("宋体",Font.BOLD,20));
        createPanel.add(priorityLabel);
        predecessorLabel = new JLabel("前驱进程:");
        predecessorLabel.setBounds(30, 240, 100, 30);
        predecessorLabel.setFont(new Font("宋体",Font.BOLD,20));
        createPanel.add(predecessorLabel);

        pidField = new JTextField(20);
        pidField.setBounds(150, 40, 160, 30);
        createPanel.add(pidField);
        runtimeField = new JTextField(20);
        runtimeField.setBounds(150, 90, 160, 30);
        createPanel.add(runtimeField);
        memoryField = new JTextField(20);
        memoryField.setBounds(150, 140, 160, 30);
        createPanel.add(memoryField);
        priorityField = new JTextField(20);
        priorityField.setBounds(150, 190, 160, 30);
        createPanel.add(priorityField);
        predecessorField = new JTextField(20);
        predecessorField.setBounds(150, 240, 160, 30);
        createPanel.add(predecessorField);

        this.add(createPanel);

        //后备队列界面
        reservePanel=new JPanel();
        reservePanel.setBounds(430,30,370,350);
        reservePanel.setLayout(new BorderLayout());

        reserveLabel=new JLabel("后备队列");
        reserveLabel.setFont(new Font("宋体",Font.BOLD,15));
        reservePanel.add(reserveLabel, BorderLayout.NORTH);

        reserveTable = new Table();
        JScrollPane scrollPane1 = new JScrollPane(reserveTable);
        reservePanel.add(scrollPane1, BorderLayout.CENTER);

        this.add(reservePanel);

        //就绪队列界面
        readyPanel=new JPanel();
        readyPanel.setBounds(820,30,370,350);
        readyPanel.setLayout(new BorderLayout());

        readyLabel=new JLabel("就绪队列");
        readyLabel.setFont(new Font("宋体",Font.BOLD,15));
        readyPanel.add(readyLabel, BorderLayout.NORTH);

        readyTable = new Table();
        JScrollPane scrollPane2 = new JScrollPane(readyTable);
        readyPanel.add(scrollPane2, BorderLayout.CENTER);

        this.add(readyPanel);

        //挂起队列界面
        hungPanel=new JPanel();
        hungPanel.setBounds(430,430,370,350);
        hungPanel.setLayout(new BorderLayout());

        hungLabel=new JLabel("阻塞队列");
        hungLabel.setFont(new Font("宋体",Font.BOLD,15));
        hungPanel.add(hungLabel, BorderLayout.NORTH);

        hungTable = new Table();
        JScrollPane scrollPane3 = new JScrollPane(hungTable);
        hungPanel.add(scrollPane3, BorderLayout.CENTER);

        this.add(hungPanel);

        //完成队列界面
        endPanel=new JPanel();
        endPanel.setBounds(820,430,370,350);
        endPanel.setLayout(new BorderLayout());

        endLabel=new JLabel("完成队列");
        endLabel.setFont(new Font("宋体",Font.BOLD,15));
        endPanel.add(endLabel, BorderLayout.NORTH);

        endTable = new Table();
        JScrollPane scrollPane4 = new JScrollPane(endTable);
        endPanel.add(scrollPane4, BorderLayout.CENTER);

        this.add(endPanel);


        //CPU界面
        cpuPanel=new JPanel();
        cpuPanel.setBounds(20,460,350,150);
        cpuPanel.setLayout(null);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("CPU运行情况");
        Font font = new Font("宋体", Font.BOLD, 18);
        titledBorder.setTitleFont(font);
        cpuPanel.setBorder(titledBorder);

        cpu1Label=new JLabel("CPU1");
        cpu1Label.setFont(new Font("宋体", Font.BOLD, 20));
        cpu1Label.setBounds(40,40,50,30);
        cpuPanel.add(cpu1Label);

        cpu2Label=new JLabel("CPU2");
        cpu2Label.setFont(new Font("宋体", Font.BOLD, 20));
        cpu2Label.setBounds(40,80,50,30);
        cpuPanel.add(cpu2Label);

        cpu1TextField=new JTextField();
        cpu1TextField.setEditable(false);
        cpu1TextField.setBounds(110,40,50,30);
        cpuPanel.add(cpu1TextField);

        cpu2TextField=new JTextField();
        cpu2TextField.setEditable(false);
        cpu2TextField.setBounds(110,80,50,30);
        cpuPanel.add(cpu2TextField);

        progressBar1=new ProgressBar(0,singleTime);
        progressBar1.setLocation(180,40);
        cpuPanel.add(progressBar1);

        progressBar2=new ProgressBar(0,singleTime);
        progressBar2.setLocation(180,80);
        cpuPanel.add(progressBar2);

        this.add(cpuPanel);

        // 设置 cpu1TextField 的焦点监听器
        // 点击前后颜色进行一个变化
        // 保存 cpu1TextField 原来的背景色
        final String[] cpu1Text = {""};
        Color originalColor = cpu1TextField.getBackground();
        cpu1TextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                cpu1TextField.setBackground(Color.WHITE);
                cpuPid = Integer.parseInt(cpu1TextField.getText());
                cpu = 1;
                System.out.println("此时的进程号"+cpuPid);
                System.out.println("选择的CPU为"+cpu);

            }
            @Override
            public void focusLost(FocusEvent e) {
                cpu1TextField.setBackground(originalColor);
            }
        });

        // 设置 cpu2TextField 的焦点监听器
        cpu2TextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                cpu2TextField.setBackground(Color.WHITE);
                cpuPid = Integer.parseInt(cpu2TextField.getText());
                cpu = 2;
                System.out.println("此时的进程号"+cpuPid);
            }
            @Override
            public void focusLost(FocusEvent e) {
                cpu2TextField.setBackground(originalColor);
            }
        });

        //内存
        mempl = new MemoryPanel();
        this.add(mempl);

        //按钮面板界面
        buttonPanel=new JPanel();
        buttonPanel.setBounds(0,650,400,170);
        buttonPanel.setLayout(null);;

        createBtn = new JButton("创建进程");
        createBtn.setBounds(20,10,170,30);
        createBtn.setFont(new Font("宋体", Font.BOLD, 20));
        buttonPanel.add(createBtn);

        closeBtn = new JButton("终止进程");
        closeBtn.setBounds(200,10,170,30);
        closeBtn.setFont(new Font("宋体", Font.BOLD, 20));
        buttonPanel.add(closeBtn);

        hookBtn = new JButton("阻塞进程");
        hookBtn.setBounds(20,50,170,30);
        hookBtn.setFont(new Font("宋体", Font.BOLD, 20));
        buttonPanel.add(hookBtn);

        unhookBtn = new JButton("唤醒进程");
        unhookBtn.setBounds(200,50,170,30);
        unhookBtn.setFont(new Font("宋体", Font.BOLD, 20));
        buttonPanel.add(unhookBtn);

        runBtn = new JButton("时间按钮");
        runBtn.setFont(new Font("宋体", Font.BOLD, 20));
        runBtn.setBounds(20,100,350,30);
        buttonPanel.add(runBtn);

        this.add(buttonPanel);

        // 给按钮组增加监听
        createBtn.addActionListener(new createActionListener());
        closeBtn.addActionListener(new closeActionListener());
        hookBtn.addActionListener(new hookActionListener());
        unhookBtn.addActionListener(new unhookActionListener());
        runBtn.addActionListener(new runActionListener());


        /**内存数组初始化 **/
        for(int i=0;i<memorySize;i++){
            if(i==0){
                memoryList[i]=9999; // 代表os
            }else if(i==1){
                memoryList[i]=osSize; // 代表os区大小
            }else{
                memoryList[i]=0;// 代表空闲区
            }
        }

        mempl.updateMemoryList(memoryList);
        this.setVisible(true);
    }

    // 为进程p分配内存
    public void allocateMemory(int PCB_pid,int PCB_memory){
        for(int i=0;i<memorySize;i++){
            if(memoryList[i]!=0&&memoryList[i+1]!=0&&memoryList[i+memoryList[i+1]]==0){// 找到空闲区的开始
                int freeBlockStart = i + memoryList[i+1];
                i = freeBlockStart;
                System.out.println("内存起始地址：：："+ freeBlockStart);
                int freeBlockSize = 0;
                while(i < memorySize && memoryList[i]==0){ // 判断空闲区是否够长
                    freeBlockSize++;
                    if(freeBlockSize == PCB_memory){
                        memoryList[freeBlockStart] = PCB_pid;
                        memoryList[freeBlockStart+1] = PCB_memory;
                        System.out.println("内存起始地址"+ freeBlockStart);
                        return;
                    }
                    i++;
                }
            }
        }
    }

    // 清除进程p的内存空间占据，此处不用考虑内存的合并
    public void clearMemory(int PCB_pid){
        for(int j=32;j<memoryList.length;j++){
            if(j+1 < memoryList.length && memoryList[j] == PCB_pid ){
                // 判断是否为要撤销的进程
                memoryList[j] = 0;
                memoryList[j+1] = 0;
            }
        }
    }

    /** 按钮组的响应事件 **/
    // 创建进程
    public class createActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1.获取组件的文本内容
            int pid= Integer.parseInt(pidField.getText());
            int runtime= Integer.parseInt(runtimeField.getText());
            int memory= Integer.parseInt(memoryField.getText());
            int pri= Integer.parseInt(priorityField.getText());
            String str=predecessorField.getText();
            System.out.println(str);
            int pre;
            if(str.equals("")){
                pre = -1;
            }else{
                pre= Integer.parseInt(str);
            }
            // 2.创建PCB
            PCB p=new PCB(pid,runtime,pri, pre, memory);
            // 3.清除文本框内容
            pidField.setText("");
            runtimeField.setText("");
            memoryField.setText("");
            priorityField.setText("");
            predecessorField.setText("");

            // 4.查重Pid
            for(PCB p1:reserveList){
                if(p.equals(p1)){
                    JOptionPane.showMessageDialog(null, "进程重复，请更换PID！", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            // 5.检查前驱节点是否在后备|就绪|CPU|挂起|完成队列
            boolean exist=false;
            if(pre!=-1){
                for(PCB p1:reserveList){
                    if(pre==p1.getPid()){
                        exist=true;
                    }
                }
                for(PCB p1:readyList){
                    if(pre==p1.getPid()){
                        exist=true;
                    }
                }
                for(PCB p1:hookList){
                    if(pre==p1.getPid()){
                        exist=true;
                    }
                }
                for(PCB p1:cpuList){
                    if(p1!=null&&pre==p1.getPid()){
                        exist=true;
                    }
                }
                for(PCB p1:endList){
                    if(pre==p1.getPid()){
                        exist=true;
                    }
                }

                if(exist==false){
                    JOptionPane.showMessageDialog(null, "前驱进程不存在！", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 6.添加到reverseList中
            reserveList.add(p);

            // 7.添加到后备列表上
            reserveTable.updateTable(reserveList,false);
        }
    }

    // 运行
    public class runActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            time ++;
            timeJlb.setText(Integer.toString(time));

            /**cpu界面置空**/
            if(cpuList[0]==null){
                cpu1TextField.setText("");
                progressBar1.reset();
            }
            if(cpuList[1]==null){
                cpu2TextField.setText("");
                progressBar2.reset();
            }

            /** CPU中的程序运行一秒 **/
            for(int i=0;i<cpuList.length;i++){
                try{
                    PCB p=cpuList[i];
                    boolean end=false;// 运行结束
                    boolean singletimeout=false;// 时间片用完

                    //剩余所需时间--，进度条++
                    p.setRequiredTime(p.getRequiredTime()-1);
                    if(p.getCpu()==1){
                        progressBar1.updateValue();
                    }else{
                        progressBar2.updateValue();
                    }

                    //判断状态
                    if(p.getRequiredTime()==0){
                        end=true;
                    }
                    if((p.getCpu()==1&&progressBar1.getValue()==singleTime)||(p.getCpu()==2&&progressBar2.getValue()==singleTime)){
                        singletimeout=true;
                    }

                    // 当进程运行结束时--改变CPUlist和memorylist和label，先不改变进度条
                    if(end==true){
                        if(p.getCpu()==1){
                            cpu1TextField.setText("");
                        }else if(p.getCpu()==2){
                            cpu2TextField.setText("");
                        }
                        cpuList[i]=null;
                        endList.add(p);
                        endTable.updateTable(endList,false);

                        //清除此进程占的内存
                        clearMemory(p.getPid());
                    }

                    // 当时间片用完但没结束的进程--放到就绪队列
                    if(singletimeout==true&&end==false){
                        if(p.getCpu()==1){
                            cpuList[0]=null;
                        }else if(p.getCpu()==2){
                            cpuList[1]=null;
                        }

                        if(p.getPriority()>-1) p.setPriority(p.getPriority()-1); // 动态优先级
                        readyList.add(p);
                        readyTable.updateTable(readyList,true);
                    }
                }catch(Exception ex){}
            }

            /** 从阻塞队列中自动判断是否解除阻塞到就绪队列：判断前驱进程是否完成 **/
            for(int i=0;i<hookList.size();i++){
                PCB p=hookList.get(i);
                // 如果完成队列没有前驱点进程，此进程应该进入阻塞状态
                if(endTable.searchPid(endList,p.getPre())==true){
                    hookList.remove(hookList.get(i));
                    hungTable.updateTable(hookList,false);

                    readyList.add(p);
                    readyTable.updateTable(readyList,true);
                    i--;
                }
            }

            /** 从就绪队列中挑选进程到CPU或阻塞队列：判断前驱结点是否正在运行 **/
            while(readyList.size() > 0) {
                PCB p = readyList.get(0);
                int pre = p.getPre();
                boolean exist = endTable.searchPid(endList,pre);

                // 独立进程或前驱进程已完成则可以进cpu1
                if (cpuList[0]==null&& (exist == true||p.getPre()==-1)) {
                    p.setCpu(1);
                    cpuList[0] = p;
                    cpu1TextField.setText(Integer.toString(p.getPid()));
                    progressBar1.reset();
                }
                else if (cpuList[1]==null&& (exist == true||p.getPre()==-1)) {
                    p.setCpu(2);
                    cpuList[1] = p;
                    cpu2TextField.setText(Integer.toString(p.getPid()));
                    progressBar2.reset();
                }
                // 前驱进程未完成进阻塞队列
                else {
                    hookList.add(p);
                    hungTable.updateTable(hookList,false);
                    System.out.println("阻塞队列： " + hookList);
                }
                readyList.remove(0);
                readyTable.updateTable(readyList,true);
            }

            /** 从后备队列挑选进程到就绪队列 **/
            int num=0;
            if(cpuList[0]!=null){
                num++;
            }
            if(cpuList[1]!=null){
                num++;
            }
            while((readyList.size()+num+hookList.size())<channel&&reserveList.size()>0){
                PCB p=reserveList.remove(0);
                reserveTable.updateTable(reserveList,false);
                readyList.add(p);
                readyTable.updateTable(readyList,true);
            }

            // 分配内存
            for(PCB pb:readyList){
                allocateMemory(pb.getPid(), pb.getMemory());
                System.out.println("分配"+pb.getPid());
            }
            mempl.updateMemoryList(memoryList);
        }
    }

    // 阻塞：对cpu正在运行的进程进行阻塞
    public class hookActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. 从CPU选择进程
            // 时间片轮转在这里一起做了
            if (cpu == 1) {
                // 当前焦点为 cpu1TextField
                System.out.println("cpu1使用中");
                cpuPid = Integer.parseInt(cpu1TextField.getText());
                System.out.println(cpuPid);
                // 将进程放进阻塞队列中
                hookList.add(cpuList[0]);
                hungTable.updateTable(hookList, false);

                // 手动撤销进程
                cpuList[0] = null;
                cpu1TextField.setText("");
                progressBar1.reset();
            } else if (cpu == 2) {
                // 当前焦点为 cpu2TextField
                cpuPid = Integer.parseInt(cpu2TextField.getText());
                hookList.add(cpuList[1]);
                hungTable.updateTable(hookList, false);

                // 手动撤销进程
                cpuList[1] = null;
                cpu2TextField.setText("");
                progressBar2.reset();
            }
            // 3. 时间片轮转下一片
        }
    }

    // 终止：从后备队列，就绪队列或者阻塞队列中挑选，回收内存（不直接从cpu运行的进程阻塞，防止系统崩溃）
    public class closeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. 从后备或就绪或阻塞队列挑选
            int selectedRow = -1;
            PCB p = null;

            try {
                selectedRow = reserveTable.getSelectedRow();
                p = reserveTable.removeRow(selectedRow);
                for (PCB pb : reserveList){
                    if(pb.equals(p)){
                        reserveList.remove(pb);
//                        clearMemory(pb.getPid());
                        break;
                    }
                }
            } catch (Exception exception) {
                System.out.println("不是从后备队列挂起");
            }

            try {
                selectedRow = readyTable.getSelectedRow();
                p = readyTable.removeRow(selectedRow);
                for (PCB pb : readyList){
                    if(pb.equals(p)){
                        readyList.remove(pb);
                        clearMemory(pb.getPid());
                        break;
                    }
                }
            } catch (Exception exception) {
                System.out.println("不是从就绪队列挂起");
            }

            try {
                selectedRow = hungTable.getSelectedRow();
                p = hungTable.removeRow(selectedRow);
                for (PCB pb : hookList){
                    if(pb.equals(p)){
                        hookList.remove(pb);
                        clearMemory(pb.getPid());
                        break;
                    }
                }
            } catch (Exception exception) {
                System.out.println("不是从阻塞队列挂起");
            }

            //2.将子孙进程终止回收
            // 后备中
            for(int i=0;i<reserveList.size();i++){
                if(p.getPid()==reserveList.get(i).getPre()){//hungTable.searchPid(hookList,reserveList.get(i).getPre())==true
                    reserveTable.removeRow(i);
                    clearMemory(reserveList.get(i).getPid());
                }
            }
            // 就绪中
            for(int i=0;i<readyList.size();i++){
                if(p.getPid()==readyList.get(i).getPre()){//hungTable.searchPid(hookList,reserveList.get(i).getPre())==true
                    readyTable.removeRow(i);
                    clearMemory(readyList.get(i).getPid());
                }
            }
            // 阻塞中
            for(int i=0;i<hookList.size();i++){
                if(p.getPid()==hookList.get(i).getPre()){//hungTable.searchPid(hookList,reserveList.get(i).getPre())==true
                    hungTable.removeRow(i);
                    clearMemory(hookList.get(i).getPid());
                }
            }
        }
    }

    // 解挂
    public class unhookActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. 从挂起队列挑选，放入就绪队列
            int row = -1;
            PCB p = null;
            try {
                row = hungTable.getSelectedRow();
                p = hungTable.removeRow(row);
                System.out.println("解挂的进程号为：" + p.getPid());
                for (PCB pb : hookList) {
                    if (pb.equals(p)) {
                        hookList.remove(pb);
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // 放进就绪队列并重新为它分配内存
            readyList.add(p);
            readyTable.updateTable(readyList, true);

            // 2. 此进程的子孙进程(阻塞)放进就绪队列
            int pre = p.getPid();
            Iterator<PCB> iter = hookList.iterator();
            while (iter.hasNext()) {
                PCB pi = iter.next();
                if (pi.getPre() == pre) {
                    // 子孙进程
                    System.out.println("子孙进程" + pi.getPid());
                    hookList.remove(pi);
                    hungTable.updateTable(hookList, false);
                    readyList.add(pi);
                    readyTable.updateTable(readyList, true);
                    iter.remove();
                }
            }
            hungTable.updateTable(hookList, false);
        }
    }

    public static void main(String args[])
    {
        new GUI();
    }
}
