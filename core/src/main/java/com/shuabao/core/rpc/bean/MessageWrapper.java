

package com.shuabao.core.rpc.bean;

import com.shuabao.core.base.ReturnCode;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Request data wrapper.
 *
 * 消息包装.
 *
 *
 */
public class MessageWrapper implements Serializable {

    private static final long serialVersionUID = 1009813828866652852L;

    private String lang = "1";
    private String code;
    private String msg;
    private Map<String, Object> data;//参数


    public void setReturnInfo(ReturnCode code, int lang) {
        setCode(String.valueOf(code.getIndex()));
        setMsg(code.getMsg(lang));
    }


    public int getLang() {
        return NumberUtils.toInt(this.lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putData(String key, Object value) {
        if (Objects.isNull(data)) {
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageWrapper{" +
                "lang='" + lang + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
