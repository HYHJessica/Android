package b19060209.iot.njupt.edu.cn.fighting;

public class NotepadBean {
    private String id;                  //记录的id
    private String notepadContent;   //记录的内容
    private String notepadTime;       //保存记录的时间
    private String deadline;          //保存截止时间
    private String flag;                //判别记录类型
    public String getFlag(){ return  flag;}
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNotepadContent() {
        return notepadContent;
    }
    public void setNotepadContent(String notepadContent) {
        this.notepadContent = notepadContent;
    }
    public String getNotepadTime() { return notepadTime; }
    public void setNotepadTime(String notepadTime) {
        this.notepadTime = notepadTime;
    }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
