package com.ssd.admin.util;

public  enum ErrorType {
    NULL("不能为空", 1),
		DATE("不是日期类型",11), 
		DIGITAL("不是数字类型", 21), 
		STRING("不是字符串类型", 31),
		ILLEGE("是非法字符或未知类型", 41),
		BOOLEAN("不是布尔类型",61);
		
		
		
		private String display;
	    private int code;

	    // 构造方法
	    private ErrorType(String display, int code) {
	        this.display = display;
	        this.code = code;
	    }
	    
	    public int getCode(){
	    	return this.code;
	    }
	    
	    public String getValue(){
	    	return name();
	    }
	    
	    public static ErrorType fromCode(int code){
	    	ErrorType[] states = ErrorType.values();
	    	for(ErrorType state: states) {
	    		if(state.getCode()==code) {
	    			return state;
	    		}
	    	}
	    	return null;
	    }

	    // 覆盖方法
	    @Override
	    public String toString() {
	        return this.display;
	    }

		public String getDisplay() {
			return display;
		}
	}
    