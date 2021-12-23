package minesweeper;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MayinTarlasi implements MouseListener{

	JFrame frame;
	Btn[][] board = new Btn[10][10];     //board olusturdum 
	int openButton; // eger 90 buton acarsam kazanmış oluyorum
	
	public MayinTarlasi() {     //constractor
		frame = new JFrame("Mayın Tarlası");
		frame.setSize(800,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(10,10));  //görünümlere ve sınırları vermeden güzel bir tablo oluşturacak
	
		
		//Bu iki for dongusu tum tabloyu geziyor
		for(int row = 0; row < board.length; row++) {   //row kadar btn ekliyoruz  
			
			for(int col = 0; col <board[0].length; col++ ) {  //row bitince alttaki row a geçmek için colum u arttırıyoruz
				Btn b = new Btn(row,col);
				frame.add(b);      // bu butonu frame e ekliyorum
				b.addMouseListener(this);  // mouse listener ı ekledim
				board[row][col] = b;   // array e de bu butonları ekliyorum
				
			}
			
		}
		
		
		
		
		
		generateMine();
		updateCount();
		//print();  // uygulamayı çalıştırana kadar düzenlerken görebilmem için
		
		
		
		frame.setVisible(true);

	}
	
	
	
	public void generateMine() {
		
		
		int i = 0;
		//10 tane mayın koyacagım rastgele oluşan yerelere göre
		while(i<10) {
			int randRow = (int) (Math.random() * board.length);  //rastgele bir row 
			int randCol = (int) (Math.random() * board[0].length); //rastegele bir colm 
			
			while(board[randRow][randCol].isMine()) {  // daha önce mayın koydugumuz yere tektar koymamak için kontrol ediyoruz ve tekrardan random sayı üretiyoruz
				
				randRow = (int) (Math.random() * board.length);  
				randCol = (int) (Math.random() * board[0].length);
				
				
			}
			
			board[randRow][randCol].setMine(true);
			i++;
		}
		
	}
	
	
	
	
	public void print() {
		
			for(int row = 0; row < board.length; row++) {  
				for(int col = 0; col <board[0].length; col++ ) {  
						
					if(board[row][col].isMine()) {   
						board[row][col].setIcon(new ImageIcon("/Users/borasezgin/eclipse OOP /eclipseSON/miniSwipper/src/mine.png"));
					}// mayınlı olan konumlara mayın iconu ekledik
					
					
					else {
						board[row][col].setText(board[row][col].getCount()+"");
						board[row][col].setEnabled(false); 
					}
					
					//tıklanan bolgede mayın yok ise mayına yakın olan sayıları açmamız lazım
			}
				
		}
		
		
	} 

	public void updateCount() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].isMine()) {
					counting(row, col);  // mayının etrafındaki alanları tek tek gezerek o alanların değerini arttırıcak
				}
			}
		}
	}
	
	
	//mayının etrafınını gezecek 
	public void counting(int row, int col) {
		for (int i = row - 1; i <= row + 1; i++) {
			for (int k = col - 1; k <= col + 1; k++) {
				try {
					int value = board[i][k].getCount();  //mayının etrafındaki sayısı aldık
					board[i][k].setCount(++value);   // mayının etrafındaki sayıyı arttırdık
				} catch (Exception e) {

				}
			}
		}
	}
	


	//bir sayıya tıkladım ve mayın yok yakınında mayın olmayan tüm sayılar acılacak
	public void open(int r,int c) {
		if(r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c].getText().length() > 0 || board[r][c].isEnabled() == false ) {
			
			//row ve col alanın dışındaysa || butona hiç tıklanılmamışşsa || butona tıklanmamışsa 
			//tüm olumsuz sartları yazdık
			
			return ; 
		}
		
		//tıklanan butonun etradındaki mayın sayısı 0 dan farklı ise
		else if(board[r][c].getCount() != 0) {
			board[r][c].setText(board[r][c].getCount()+""); //sadece o kısmı ac
			board[r][c].setEnabled(false);
			
			openButton++;
			
		}
		
		// etrafında mayın yoksa open methodunu cagır ve sagına soluna bak acamya devam et amyın yoksa
		else {
			openButton++;

			board[r][c].setEnabled(false);
			open(r-1,c);
			open(r+1,c);
			open(r,c-1);
			open(r,+1);
		}
		
		
	}
	
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) { 
		Btn b = (Btn) e.getComponent();  // tıklanan butonun özelliklerine erişme
		
		if(e.getButton() == 1 ) { 
				
				if(b.isMine()) {
					JOptionPane.showMessageDialog(frame, "Mayına Bastınız"); //eger kullanıcı mayın olan yere tıklarsa
					print();  // oyunu kaybettik demektir ve tüm butonları göstericek
				}
				
				
				//152. satırdaki işlemi yapıyoruz tıklama ile 
				else {
					open(b.getRow(),b.getCol());
					
					//eger acılan buton row * col -10 u kadar açılmışşa kazanmışızdır
					if (openButton == (board.length * board[0].length)-10)
					{
						JOptionPane.showMessageDialog(frame, "Oyunu kazandınız"); 

					}
				}
				
				
				
			
			}//bu sol click tıklandı
		 
		
		
			
				else if (e.getButton() == 3) { 
					
						if(!b.isFlag()) {
								b.setIcon(new ImageIcon("/Users/borasezgin/eclipse OOP /eclipseSON/miniSwipper/src/flag.png"));
								b.setFlag(true);  //tıkladığımız yerde bayrak yoksa oraya bayrak ekledim
						}
							else {
								b.setIcon(null);
								b.setFlag(false);
				 
							}
			
			}//bu sag click tıklandı
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
