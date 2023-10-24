package os;
import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JProgressBar {
    public ProgressBar(int min, int max) {
        super(min, max);
        super.setValue(0);
        super.setStringPainted(true); // 显示字符串：进度百分比
        super.setForeground(Color.GRAY);
        super.setSize(140,30);
    }

    // 更新进度条值+1
    public void updateValue() throws Exception {
        super.setValue(getValue()+1);
        if(getValue()!=5){ // 5是时间片大小
            super.setForeground(Color.RED);
        }else if(getValue()==5){
            super.setForeground(Color.GREEN);
        }

    }

    // 进度条置零
    public void reset(){
        super.setValue(0);
        super.setForeground(Color.GRAY);
    }

}

