package dto;

// 解析返回的json需要的类结构
public class TranslateResultDTO {
    String from;
    String to;
    String[] trans_result;
    String src;
    String dst;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(String[] trans_result) {
        this.trans_result = trans_result;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
