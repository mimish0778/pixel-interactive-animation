import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import javax.sound.sampled.*;


public class InteractiveAnimation extends Frame implements Runnable {
    // カウント用クロック
    int myClock = 0;
    // ウィンドウの幅と高さ
    int sizeW = 600;
    int sizeH = 600;
    // 操作キャラの座標
    int playerX = sizeW / 2 - 50;
    int playerY = sizeH / 2 + 50;
    // ポケモンの座標
    int latiosX;
    int latiosY = playerY - 50;
    // ポールの座標
    int ballX = sizeW /2 - 50;
    int ballY = sizeH - 100;
    // 背景の現在のx座標
    int backgroundX = 0;
    // タイトルの座標
    int titleX = sizeW / 4 - 10;
    int titleY = 55;
    // タイトルの幅と高さ
    int titleW = 303;
    int titleH = 70;
    // マウスのｘ座標の記録用変数 Mx,　ｙ座標の記録用変数 My
    int Mx = 0;
    int My = 0;
    Graphics g;
    Graphics2D g2;
    Frame myFrame;
    Image img[];
    Clip clip[];
    // 操作キャラの向く方向 (0:left/1:right)
    int myDir1 = 1;
    // myDir1の反対の方向 (0:left/1:right)
    int myDir2 = 0;
    // 操作キャラの状態 (idle:0/walk:1/run:2/throw:3)
    int mySign = 0;
    // ポケモンが出ているか (true:1/false:0)
    int latiosFlg = 0;
    // ボールの状態 (0/push:1)
    int ballFlg = 0;
    // タイトルの状態 (0/push:1)
    int titleFlg = 0;
    // 画像を使える状態にしたか (×:0/〇:1)
    int imgFlg = 0;
  
    public static void main(String[] args) {
        InteractiveAnimation pj = new InteractiveAnimation();
    }

    public InteractiveAnimation() {
        super("Pixel Interactive Animation");

        myFrame = this;
        g = myFrame.getGraphics();
        g2 = (Graphics2D) g;
        setBackground(new Color(50, 50, 50));
        // スレッド
        Thread th;
        th = new Thread(this);
        th.start();
        
        Toolkit tk = getToolkit();
        img = new Image[8];
        
        img[0] = tk.getImage("src/images/player_idle.gif");
        img[1] = tk.getImage("src/images/player_walk.gif");
        img[2] = tk.getImage("src/images/player_run.gif");
        img[3] = tk.getImage("src/images/player_throw.gif");
        img[4] = tk.getImage("src/images/latios.gif");
        img[5] = tk.getImage("src/images/background.gif");
        img[6] = tk.getImage("src/images/ball.gif");
        img[7] = tk.getImage("src/images/ball2.gif");

        int numSounds = 5;
        clip = new Clip[numSounds];
        
        clip[0] = ClipProB.createClip(new File("src/sounds/menu_sound.wav"));
        clip[1] = ClipProB.createClip(new File("src/sounds/heal_sound.wav"));
        clip[2] = ClipProB.createClip(new File("src/sounds/get_sound.wav"));
        clip[3] = ClipProB.createClip(new File("src/sounds/pc_sound.wav"));
        clip[4] = ClipProB.createClip(new File("src/sounds/get_sound.wav"));
        
        this.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ボタンを押したか
                Mx = e.getX();
                My = e.getY();
                if ((ballX <= Mx) && (Mx <= ballX + 100) && (ballY <= My) && (My <= ballY + 100)) {
                    clip[0].loop(1);
                    mySign = 3;
                    latiosFlg = Math.abs(latiosFlg - 1);
                    ballFlg = 1;
                }
                if ((titleX <= Mx) && (Mx <= titleX + titleW) && (titleY <= My) && (My <= titleY + titleH)) {
                    titleFlg = 1;
                    int num;
                    num = (int)(Math.random() * 5);
                    clip[num].loop(1);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (mySign != 3){
                    mySign = 1;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (mySign != 3){
                    mySign = 0;
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (mySign != 3){
                    mySign = 1;
                }
            }
        });

        this.addMouseMotionListener(
                new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mySign != 3){
                    mySign = 2;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // 方向判定及び決定
                Mx = e.getX();
                if (sizeW / 2 < Mx ) {
                    myDir1 = 1;
                    myDir2 = 0;
                } else {
                    myDir1 = 0;
                    myDir2 = 1;
                }
            }
        });
        
        addWindowListener(new SampleWindowListner());
        setSize(sizeW, sizeH);
        setVisible(true);
    }
    
    // まとめて画像を配置する
    public void setImage() {
        Graphics g = myFrame.getGraphics();
        //System.out.println("画像を配置します");
        if (imgFlg == 0) {
            for (int i = 0; i < 8; i++) {
                g.drawImage(img[i], 9999, 9999, myFrame);
            }
            for (int j = 0; j < 5; j++) {
                clip[j].loop(0);
            }
            imgFlg = 1;
        }
        // ポケモンの表示
        if (latiosFlg == 1) {
            if ((myDir1 == 1) && (latiosX <= playerX - 150) || (myDir1 == 0) && (playerX + 50 <= latiosX)) {
                latiosX = playerX - 150 + (200 * myDir2);
            } else {
                latiosX += 25 * (myDir2 * 2 - 1);
            }
            latiosY += 2 * (myClock % 5 - 2);
            g.drawImage(img[4], latiosX, latiosY, latiosX + 200, latiosY + 100, 200 * myDir2, 0, 200 * myDir1, 100, myFrame);
        } else {
            latiosX = playerX - 150 + (200 * myDir2);
        }
        // 操作キャラの表示
        if (mySign != 3) {
            g.drawImage(img[(myClock % 2) * mySign], playerX, playerY, playerX + 100, playerY + 100, 100 * myDir2, 0, 100 * myDir1, 100, myFrame);
        } else {
           g.drawImage(img[3], playerX, playerY, playerX + 100, playerY + 100, 100 * myDir2, 0, 100 * myDir1, 100, myFrame);
           mySign = 1;
        }
    }

    @Override
    public void paint(Graphics g) {
        this.g = g;
        
        g.setColor(new Color(80 + (20 * titleFlg), 80 + (20 * titleFlg), 80 + (20 * titleFlg)));
        g.fillRect(titleX - 10, titleY - 6, titleW + 20, titleH + 12);
        g.setColor(new Color(192 - (70 * titleFlg), 192 - (70 * titleFlg), 192 - (70 * titleFlg)));
        g.fillRect(titleX, titleY, titleW, titleH);
        titleFlg = 0;
        
        Font f1= new Font("TimesRoman", Font.BOLD|Font.ITALIC,34);
        g.setFont(f1);
        g.setColor(new Color(30, 0, 0));
        g.drawString("ポ ッ モ ス ー", sizeW / 4 + 2, 97);
        g.drawString("ケ ト ン タ", sizeW / 4 + 27, 97);
        
        Font f2 = new Font("TimesRoman", Font.BOLD|Font.ITALIC, 34);
        g.setFont(f2);
        g.setColor(new Color(240, 100, 100));
        g.drawString("ポ ッ モ ス ー", sizeW / 4, 95);
        g.drawString("ケ ト ン タ", sizeW / 4 + 25, 95);
        
        Font f3 = new Font("TimesRoman", Font.BOLD|Font.ITALIC, 30);
        g.setFont(f3);
        g.setColor(new Color(60, 0, 0));
        g.drawString("ルビー", 335, 116);
        
        Font f4 = new Font("TimesRoman", Font.BOLD|Font.ITALIC, 30);
        g.setFont(f4);
        g.setColor(new Color(200, 50, 50));
        g.drawString("ルビー", 333, 114);
        
        Font f5 = new Font("TimesRoman", Font.BOLD, 20);
        g.setFont(f5);
        g.setColor(new Color(255, 255, 255));
        g.drawString("Pokemon", sizeW / 4 + 55, 114);
        g.drawString("´", sizeW / 4 + 95, 113);

        // 背景の描写
        if ((mySign == 1) || (mySign == 2)) {
            backgroundX +=  30 * (myDir2 * 2 - 1);
            if (backgroundX <= - 600) {
                backgroundX += 800;
            } else if (600 <= backgroundX) {
                backgroundX -= 800;
            }
        }
        
        g.drawImage(img[5], backgroundX, sizeH / 2 - 150, this);
        g.drawImage(img[5], backgroundX + 800, sizeH / 2 - 150, this);
        g.drawImage(img[5], backgroundX - 800, sizeH / 2 - 150, this);
        g.drawImage(img[6 + ballFlg], ballX, ballY, this);
        // flgのリセット
        ballFlg = 0;
    }
    
    public void run() {
        try {
            while (true) {
                Thread.sleep(500 / (mySign + 1));
                myClock++;
                repaint();
                Thread.sleep(10);
                setImage();
            }
        } catch (InterruptedException e) {
        }
    }

    //　ウィンドウを閉じる動作で行う処理
    class SampleWindowListner extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {

            System.exit(0);
        }
    }

}
