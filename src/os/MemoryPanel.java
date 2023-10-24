package os;

import javax.swing.*;
import java.awt.*;

public class MemoryPanel extends JPanel {
    private int[] memoryList;
    private int tip = 0;
    public MemoryPanel() {
        // 初始化操作
        this.setLayout(null);
        this.setBounds(1240, 45, 170, 730);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void updateMemoryList(int[] memoryList) {
        // 更新内存数据
        this.memoryList = memoryList;
        System.out.println("updatedates");
        tip = 1;
        // 重新绘制组件
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 确保在绘制更新的矩形和文本之前清除任何先前绘制的图形
        super.paintComponent(g);
        // 绘制内存图形
        // 设置标志词，初始化的时候不用重绘面板；只有当更新数据的时候才重绘面板
        if(tip == 1)
        {
            System.out.println("repaint");
            Color[] colorList = new Color[5];
            colorList[0] = new Color(199, 237, 233);
            colorList[1] = new Color(175, 215, 237);
            colorList[2] = new Color(92, 167, 186);
            colorList[3] = new Color(255, 66, 93);
            colorList[4] = new Color(147, 224, 255);
            int color = 0;  // 标识现在用什么画笔颜色
            int start = 0;  // 每一次绘画的起始坐标
            int pid = -1;   // 进程号
            int mem = -1;   // 内存大小
            int rev = this.getHeight()/256;
            Graphics2D g2 = (Graphics2D) g;
            for(int i = 0; i < memoryList.length; i++){
                if(memoryList[i] == 9999){
                    g2.setStroke(new BasicStroke(2)); // 设置线宽为5像素
                    // 代表是os，只需要画其中一个
                    g2.setColor(new Color(248,232,137));
                    g2.drawRect(0, start, 170, rev*32);
                    g2.fillRect(0, start, 170, rev*32);   // 填充矩形颜色
                    Font font = new Font("SimSun", Font.BOLD, 12);
                    g2.setFont(font);
                    g2.setColor(Color.BLACK);
                    g2.drawString("OS", 80, (start+rev*32)/2 +5); // 绘制文字
                    start += 32 * rev;
                    i += 31;
                }else if(memoryList[i] != 0 && memoryList[i+1] != 0){
                    pid = memoryList[i];
                    mem = memoryList[i+1];
                    System.out.println("内存大小为："+ mem);
                    g2.setStroke(new BasicStroke(5)); // 设置线宽为5像素
                    g2.setColor(colorList[color]);
                    color ++ ;
                    g2.drawRect(0, i*rev, 170, mem* rev);
                    g2.fillRect(0, i*rev, 170, mem* rev);   // 填充矩形颜色
                    Font font = new Font("SimSun", Font.BOLD, 12);
                    g2.setFont(font);
                    g2.setColor(Color.BLACK);
                    g2.drawString(("PID:"+ pid ), 70, i*rev + 2*(mem*rev)/5);
                    g2.drawString(("起始地址:"+ i + " 内存大小:"+ mem), 10, i*rev + 3*(mem*rev)/4);
                    i += mem - 2;
                    System.out.println("此时下标为"+i);
                }else{  // 内存空闲
                }
            }
        }
    }
}