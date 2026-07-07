
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ClipProB {
 
    
     /*　以下のメソッドは、変更しないでください。
　　　　オーディオファイル再生のための仕組みです。　*/
    public static Clip createClip(File path) {
        //オーディオ入力ストリームの取得
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)) {
            //オーディオファイルの形式取得
            AudioFormat af = ais.getFormat();
            //オーディオファイルの情報からデータラインオブジェクトを生成
            DataLine.Info dataLine = new DataLine.Info(Clip.class, af);
            //指定された Line.Info の指定内容に一致するオーディオのラインを取得
            Clip c = (Clip) AudioSystem.getLine(dataLine);
            //指定フィルを再生する
            c.open(ais);
            return c;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }
}
