package os;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable {
    public Table() {
        super(new DefaultTableModel(new String[]{"PID", "前驱进程", "运行时间","内存大小", "优先级"},12));
        setRowHeight(30);
        setFont(new Font("宋体", Font.BOLD, 15));
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 自动调整列的宽度
        setShowVerticalLines(true); // 显示表格的垂直网格线
        setFillsViewportHeight(true); // 使表格填充整个可见区域的高度
    }

    /**这个方法首先获取 JTable 的模型，然后将传入的 ArrayList 中的每个 PCB 对象转换为一行数据，
     * 并使用 setValueAt() 方法将数据设置到表格中。由于 JTable 的行数固定为 10，所以我们需要
     * 对传入的数据进行处理，使其不超过 10 行。最后，我们用空白行填充剩余的行，确保表格总共有 10 行。**/
    public void updateTable(ArrayList<PCB> data,boolean flag) {
        // 封装了排序功能
        if(flag==true){
            Collections.sort(data);
        }

        DefaultTableModel model = (DefaultTableModel) getModel();
        int rows = Math.min(data.size(), 12);
        int columns = getColumnCount();

        for (int i = 0; i < rows; i++) {
            PCB pcb = data.get(i);
            Object[] row = new Object[columns];
            row[0] = pcb.getPid();
            row[1] = pcb.getPre();
            row[2] = pcb.getRequiredTime();
            row[3] = pcb.getMemory();
            row[4] = pcb.getPriority();
            for (int j = 0; j < columns; j++) {
                model.setValueAt(row[j], i, j);
            }
        }
        for (int i = rows; i < 10; i++) {
            Object[] row = new Object[columns];
            for (int j = 0; j < columns; j++) {
                row[j] = "";
                model.setValueAt(row[j], i, j);
            }
        }

    }

    /**查找表中是否有某个前驱为pre的进程**/
    public boolean searchPid(ArrayList<PCB> arrayList,int pre) {
        for(PCB p:arrayList){
            if(p.getPid()==pre){
                return true;
            }
        }
        return false;
    }

    /** 去掉某一行的数据并且返回当前行的进程 **/
    public PCB removeRow(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        int columnCount = model.getColumnCount();

        int pid = (int)model.getValueAt(selectedRow, 0);
        int Pre = (int)model.getValueAt(selectedRow, 1);
        int RequiredTime = (int)model.getValueAt(selectedRow, 2);
        int Memory = (int)model.getValueAt(selectedRow, 3);
        int Priority = (int)model.getValueAt(selectedRow, 4);

        if (selectedRow != -1) {
            model.removeRow(selectedRow);
            return new PCB( pid,  RequiredTime,  Priority,  Pre,  Memory);
        } else {
            return null;
        }
    }

}
