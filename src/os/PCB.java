
package os;
public class PCB implements Comparable<PCB>{
    private int Pid;   // 进程名
    private int RequiredTime;  // 运行时间
    private int Priority;  // 优先级
    private int Pre;     // 前驱点
    private int Memory;  // 内存大小
    private int ComeTime; // 进入CPU时间
    private  int cpu;//进入的cpu序号

    // 初始化，这里初始化ComeTime = 0
    public PCB(int pid, int RequiredTime, int Priority, int Pre, int Memory){
        this.Pid = pid;
        this.RequiredTime = RequiredTime;
        this.Priority = Priority;
        this.Pre = Pre;
        this.Memory = Memory;
        this.ComeTime = -1;
        this.cpu=-1;
    }

    // 外部获取进程号
    public int getPid() {
        return Pid;
    }

    // 外部获取运行时间
    public int getRequiredTime() {
        return RequiredTime;
    }

    // 外部获取优先级
    public int getPriority() {
        return Priority;
    }

    // 外部获取前驱点
    public int getPre(){
        return Pre;
    }

    // 外部获取内存大小
    public int getMemory(){
        return Memory;
    }

    // 外部获取cpu序号
    public int getCpu() {
        return cpu;
    }

    // 外部设置此进程进入CPU的起始时间
    public void setComeTime(int time){
        this.ComeTime = time;
    }

    // 外部设置此进程进入CPU序号
    public void setCpu(int cpu){
        this.cpu = cpu;
    }


    // 外部设置此进程剩余需要的时间
    public void setRequiredTime(int time){
        this.RequiredTime = time;
    }

    // 外部设置此进程优先级
    public void setPriority(int Priority){
        this.Priority = Priority;
    }

    // 比较两进程是否相等
    public boolean equals(PCB other){
        if (other.getPid() == this.Pid){
            return true;
        }
        else
            return false;
    }

    // 返回字符形式
    @Override
    public String toString() {
        return Pid + " " + RequiredTime + " " + Priority + " " +Pre + " " + Memory;
    }

    @Override
    public int compareTo(PCB o) {
        return o.getPriority() - this.Priority; // 降序排列
    }
}