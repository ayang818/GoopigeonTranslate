package MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalwork.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HistoryFragment extends Fragment {
    public TextView textView;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.view_history, null);
        textView = (TextView) view.findViewById(R.id.history);
        String historyResult = bufferRead("/data/user/0/com.example.finalwork/files/data.txt");
        textView.setText(historyResult);
        return view;
    }

    // 获取历史纪录
    public static String bufferRead(String filePath) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filePath));
            String line = bfr.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bfr.readLine();
            }
            bfr.close();
            Log.d("buffer", "bufferRead: " + sb.toString());
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
