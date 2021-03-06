package ua.com.ex.tools.parser;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.ex.exception.ToolsException;
import ua.com.ex.tools.file.FileOperation;

@Component(value="fileSqlParser")
public class FileSqlParser { 

    private static final Logger logger = LoggerFactory.getLogger(FileSqlParser.class);

    @Autowired
    private FileOperation fileOperation;

    private static final String PATTERN_GET_ALL_FIELDS = "\\(.*?\\),|\\(.*?\\);";

    public  ArrayList<ArrayList<String>> get (String start, String end, String fileNameIn, int size) throws Exception{
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String itemQueryAll = getItemQueryAll(start, end, fileNameIn);                        
        Pattern p = Pattern.compile(PATTERN_GET_ALL_FIELDS);
        Matcher  mat = p.matcher(itemQueryAll);
        while (mat.find()) {                                  
            ArrayList<String> itemQuery = getItem(mat.group());                
            if (itemQuery.size() != size ){
                String errorMessage = "FileSqlParser.get() incorrect quantity of fields = "+itemQuery.size();
                logger.error(errorMessage);
                throw new ToolsException(errorMessage);
            }
            result.add(itemQuery);
        }
        return result;        
    }

    private String getItemQueryAll(String startMarker, String endMarker, String inputFileName) throws Exception {  
        String result = fileOperation.readTextFile(inputFileName);
        boolean isContainStartMarker = result.contains(startMarker) ;
        boolean isContainEndMarker = result.contains(endMarker) ;
        if(isContainStartMarker && isContainEndMarker){
            result = StringUtils.substringAfter(result, startMarker );
            result = StringUtils.substringBefore(result, endMarker);
        } else {
            String errorMessage = "FileSqlParser.getItemQueryAll() isContainStartMarker = "+ isContainStartMarker + 
                    " isContainEndMarker = "+isContainEndMarker;            
            logger.error(errorMessage);
            throw new ToolsException(errorMessage);
        }
        return result;  
    }

    private ArrayList<String> getItem(String sourceQuery){ 
        sourceQuery = sourceQuery.substring(1, sourceQuery.length());
        sourceQuery = sourceQuery.substring(0, sourceQuery.length()-2); 
        ArrayList<String> result = new ArrayList<>();        
        Buffer buffer = new BufferForDigit();
        for(int i = 0; i< sourceQuery.length(); i++){
            char current = sourceQuery.charAt(i);
            if(buffer.isWork){
                if (isLast(sourceQuery, i)){
                    result.add(buffer.getResult());
                } else {
                    if(buffer.isDelimiter(current)){                    
                        result.add(buffer.getResult());
                        buffer.isWork = false;
                    }
                    else {                    
                        buffer.add(current); 
                    }
                }
            }
            else{                
                if(current!=','){
                    buffer = getBuffer(current);                    
                }
            }
        }
        return result;
    }

    private boolean isLast(String sourceQuery, int i) {
        return i == sourceQuery.length()-1;
    }

    private abstract class Buffer{
        public String buffer ="";
        public boolean isWork = true;
        public abstract boolean isDelimiter(char current);

        public void add(char current){
            buffer+= current;
        }

        public String getResult(){
            return buffer;
        }
    }

    private class BufferForDigit extends Buffer{
        @Override
        public boolean isDelimiter(char current) {           
            return current==',';
        }
    }

    private class BufferForNull extends Buffer{
        @Override
        public boolean isDelimiter(char current) {            
            return current ==',';
        }
    }

    private class BufferForText extends Buffer{
        @Override
        public boolean isDelimiter(char current) {            
            return current =='\'';
        }
    }

    private Buffer getBuffer(char current){        
        if (Character.isDigit(current)){
            BufferForDigit buffer = new BufferForDigit();
            buffer.add(current);            
            return buffer;
        }
        if (current =='\''){            
            return new BufferForText();
        }
        else {           
            BufferForNull buffer = new BufferForNull();
            buffer.add(current);
            return buffer;
        }
    }

}
