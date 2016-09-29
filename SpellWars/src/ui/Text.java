package ui;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.GameMap;
import mechanic.Point;
import states.StateGame;
/*
 * DESCRIPTION
 * 
 * A text object for displaying text
 */
//LOC IS ACTUALLY TOP LEFT
//PIPE SYMBOL (|) IS USED FOR NEWLINE
//UPPERCASE = BOLD
public class Text extends UIElement {
	public static final int MAX_CHARACTER_VALUE = 255;
	static Image[] characterImages = new Image[MAX_CHARACTER_VALUE];
	static String[] characterImagePaths = new String[MAX_CHARACTER_VALUE];
	static final Character[] USABLE_SPECIAL_CHARACTERS = {' ','\'',':',',','-','!','(','%','.','+','?',')',';','/'};
	static final Character NEW_LINE = '|';
	static boolean alreadyLoadedImages = false;
	boolean useOutline;
	int outlineWidth;
	Color outlineColor;
	String text;
	TextFormat format;
	Image[] images;
	Color color;
	int boxWidth;
	int kerning;
	int lineSpacing;
	int letterWidth;
	int letterHeight;
	int lines;
	//Text(ui, loc, boxWidth, letterWidth, letterHeight, kerning, lineSpacing, color, text, textFormat)
	public Text(UI ui, Point loc, int boxWidth, int letterWidth, int letterHeight, int kerning, int lineSpacing, Color color, String text, TextFormat format) {
		super(ui, loc);
		this.boxWidth = boxWidth;
		this.letterWidth = letterWidth;
		this.letterHeight = letterHeight;
		this.lineSpacing = lineSpacing;
		this.kerning = kerning;
		this.color = color;
		this.outlineColor = Color.black;
		this.text = text;
		this.format = format;
		this.outlineWidth = 1;
		lines = -1;
		
		if(alreadyLoadedImages == false) {
			for(int i = 0; i < MAX_CHARACTER_VALUE; i++) {
				if(isUsableSpecialCharacter(i) || (i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122) | i == 124) {
					characterImages[i] = this.loadImageFromCharacter((char)i);
					characterImagePaths[i] = this.getImagePathFromCharacter((char)i);
				}
			}
			System.out.println("Text images successfully loaded!");
			alreadyLoadedImages = true;
		}
	}
	public Text(UI ui, Point loc, int boxWidth, float scale, int kerning, int lineSpacing, Color color, String text, TextFormat format) {
		this(ui, loc, boxWidth, (int)(scale * 6), (int)(scale * 8), kerning, lineSpacing, color, text, format);
	}
	public Text(UI ui, Point loc, int boxWidth, float scale, Color color, String text, TextFormat format) {
		this(ui, loc, boxWidth, (int)(scale * 6), (int)(scale * 8), (int)(scale * 7), (int)(scale * 10), color, text, format);
	}
	public Text(UI ui, Point loc, String text) {
		this(ui, loc, 200, 1, Color.white, text, TextFormat.LEFT_JUSTIFIED);
	}
	public Text(UI ui, Point loc){
		this(ui, loc, "");
	}
	@Override
	public void draw(Graphics g) {
		Point currCharaOffset = new Point();
		lines = 1;
		/*
		StringTokenizer st = new StringTokenizer(text);
		for(int i = 0; i < st.countTokens(); i++) {
			String word = st.nextToken();
			if(currCharaOffset.getX() + this.letterWidth + this.kerning * word.length() > this.boxWidth) { //IF THE TEXT IS GONA GO OUT OF THE BOX
				currCharaOffset.changeX(0);
				currCharaOffset.addY(lineSpacing);
			}
			for(int j = 0; j < word.length(); j++) {
				Point finalPoint = Point.clone(currCharaOffset);
				finalPoint.add(this.loc);
				this.drawCharacter(finalPoint, word.charAt(j), g);
				currCharaOffset.addX(kerning);
			}
			currCharaOffset.addX(kerning); //SPACE BETWEEN WORDS
		}
		*/
		if(this.format == TextFormat.LEFT_JUSTIFIED) {
			int currWordIndex = 0;
			for (int i = 0; i < this.text.length(); i++) {
				//this.drawCharacter(finalPoint, i, g);
				if(this.text.charAt(i) == ' ' || i == this.text.length() - 1) {
					if(currCharaOffset.getX() > this.boxWidth || (i == this.text.length() - 1 && currCharaOffset.getX() + this.letterWidth > this.boxWidth)) {
						currCharaOffset.changeX(-kerning); //TO BALANCE OUT THE SPACE
						currCharaOffset.addY(this.lineSpacing);
						this.lines++;
					} else {
						currCharaOffset.addX(-kerning * (i - currWordIndex)); //go back to start of word's position
					}
					for(int j = currWordIndex; j <= i; j++) {
						Point finalPoint = Point.clone(currCharaOffset);
						finalPoint.add(this.getFinalLoc());
						this.drawCharacter(finalPoint, j, g, Character.isUpperCase(this.text.charAt(j)));
						currCharaOffset.addX(kerning);
					}
					currWordIndex = i;
				} else if(this.text.charAt(i) == NEW_LINE) {
					if(currCharaOffset.getX() > this.boxWidth) {
						currCharaOffset.changeX(-kerning);
						currCharaOffset.addY(this.lineSpacing);
						this.lines++;
					} else {
						currCharaOffset.addX(-kerning * (i - currWordIndex)); //go back to start of word's position
					}
					for(int j = currWordIndex; j <= i; j++) {
						Point finalPoint = Point.clone(currCharaOffset);
						finalPoint.add(getFinalLoc());
						this.drawCharacter(finalPoint, j, g, Character.isUpperCase(this.text.charAt(j)));
						currCharaOffset.addX(kerning);
					}
					currWordIndex = i;
					currCharaOffset.changeX(0);
					currCharaOffset.addY(this.lineSpacing);
					this.lines++;
				} else {
					currCharaOffset.addX(kerning);
				}
			}
		} else {
			int currWordIndex = 0;
			int numLines = 0;
			int characterDrawIndex = 0;
			float tempx = 0;
			ArrayList<String> textLines = new ArrayList<String>();
			String addbuffer = "";
			for(int i = 0; i < this.text.length(); i++) {
				if(this.text.charAt(i) == ' ' || i == this.text.length() - 1) {
					String trimmedLine = new String(addbuffer).trim();
					float lineWidth = (trimmedLine.length() * this.kerning) + this.letterWidth - this.kerning; //THE WIDTH (IN PIXELS) OF THE LINE
					if(tempx > this.boxWidth || (i == this.text.length() - 1 && tempx + this.letterWidth > this.boxWidth)) {
						tempx = -kerning; //TO BALANCE OUT THE SPACE
						textLines.add(addbuffer);
						addbuffer = "";
						numLines++;
					} else {
						tempx += -kerning * (i - currWordIndex); //go back to start of word's position
					}
					for(int j = currWordIndex; j < i; j++) {
						addbuffer += this.text.charAt(j);
						tempx += kerning;
					}
					if(i == this.text.length() - 1) {
						addbuffer += this.text.charAt(i);
						textLines.add(addbuffer);
						addbuffer = "";
						numLines++;
					}
					currWordIndex = i;
					tempx += kerning;
				} else if(this.text.charAt(i) == NEW_LINE) {
					if(tempx > this.boxWidth) {
						tempx = -kerning;
						textLines.add(addbuffer);
						addbuffer = "";
						numLines++;
					} else {
						tempx += -kerning * (i - currWordIndex); //go back to start of word's position
					}
					for(int j = currWordIndex; j < i; j++) {
						addbuffer += this.text.charAt(j);
						tempx += kerning;
					}
					currWordIndex = i;
					tempx = 0;
					textLines.add(addbuffer);
					addbuffer = "";
					numLines++;
				} else {
					tempx += kerning;
				}
			}
			this.lines = numLines;
			if(this.format == TextFormat.CENTER_JUSTIFIED) {
				for(int i = 0; i < numLines; i++) {
					String trimmedLine = new String(textLines.get(i)).trim();
					if(trimmedLine.length() > 0 && trimmedLine.charAt(0) == NEW_LINE) {
						if(trimmedLine.length() > 1) {
							trimmedLine = trimmedLine.substring(1);
						} else {
							trimmedLine = "";
						}
					}
					float lineWidth = (trimmedLine.length() * this.kerning) + this.letterWidth - this.kerning; //THE WIDTH (IN PIXELS) OF THE LINE
					currCharaOffset.changeX((this.boxWidth - lineWidth)/2);
					for(int j = 0; j < trimmedLine.length(); j++) {
						Point finalPoint = Point.clone(currCharaOffset);
						finalPoint.add(getFinalLoc());
						this.drawCharacter(finalPoint, characterDrawIndex, g, Character.isUpperCase(this.text.charAt(characterDrawIndex)));
						characterDrawIndex++;
						currCharaOffset.addX(kerning);
					}
					characterDrawIndex++; //SKIP THE SPACE CHARACTER, WHICH WAS USED TO START THE NEXT LINE
					currCharaOffset.addY(lineSpacing); //NEXT LINE
				}
			}
			if(this.format == TextFormat.RIGHT_JUSTIFIED) {
				for(int i = 0; i < numLines; i++) {
					String trimmedLine = new String(textLines.get(i)).trim();
					if(trimmedLine.length() > 0 && trimmedLine.charAt(0) == NEW_LINE) {
						if(trimmedLine.length() > 1) {
							trimmedLine = trimmedLine.substring(1);
						} else {
							trimmedLine = "";
						}
					}
					float lineWidth = (trimmedLine.length() * this.kerning) + this.letterWidth - this.kerning; //THE WIDTH (IN PIXELS) OF THE LINE
					currCharaOffset.changeX(this.boxWidth - lineWidth);
					for(int j = 0; j < trimmedLine.length(); j++) {
						Point finalPoint = Point.clone(currCharaOffset);
						finalPoint.add(getFinalLoc());
						this.drawCharacter(finalPoint, characterDrawIndex, g, Character.isUpperCase(this.text.charAt(characterDrawIndex)));
						characterDrawIndex++;
						currCharaOffset.addX(kerning);
					}
					characterDrawIndex++; //SKIP THE SPACE CHARACTER, WHICH WAS USED TO START THE NEXT LINE
					currCharaOffset.addY(lineSpacing); //NEXT LINE
				}
			}
		}
		if(this.getMap() != null){
			int drawoutline = 0;
			if(this.useOutline) {
				drawoutline = 1;
			}
			if(StateGame.isServer && this.sendDrawInfo)
			this.getMap().addToDrawInfo(GameMap.getDrawDataT(this.text, this.getFinalLoc().x, this.getFinalLoc().y, this.boxWidth, this.letterWidth, this.letterHeight, this.kerning, this.lineSpacing, this.format, drawoutline, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha(), this.outlineColor.getRed(), this.outlineColor.getGreen(), this.outlineColor.getBlue(), this.outlineColor.getAlpha()));
		}
	}
	public void drawCharacter(Point loc, int index, Graphics g, boolean isUppercase) {
		Image characterImage = characterImages[this.text.charAt(index)].getScaledCopy(this.letterWidth, this.letterHeight);
		String characterImagePath = characterImagePaths[this.text.charAt(index)];
		characterImage.setAlpha(this.getAlpha());
		if(useOutline && !isUppercase) {
			/* OBLITERATES FRAMERATE
			ArrayList<Point> points = getOutlinePoints(loc, this.letterWidth * 0.0833, this.letterHeight * 0.0633, this.outlineWidth);
			for(Point p : points) {
				g.drawImage(characterImages[this.text.charAt(index)].getScaledCopy(this.letterWidth, this.letterHeight), (float)p.getX(), (float)p.getY(), outlineColor);
			}
			*/
			
			g.drawImage(characterImage, (float)(loc.getX() + this.letterWidth * 0.0833), (float)(loc.getY()), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() + this.letterHeight * 0.0633), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() - this.letterWidth * 0.0833), (float)(loc.getY()), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() - this.letterHeight * 0.0633), outlineColor);

			/*g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() - this.letterHeight * 0.0633), outlineColor);
			if(this.getMap() != null) {
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() + this.letterWidth * 0.0833, loc.getY(), this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() + this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() - this.letterWidth * 0.0833, loc.getY(), this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() - this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
			}
			*/
		}
		if(useOutline && isUppercase) {
			/* DESTROYS FRAMERATE WTF
			ArrayList<Point> points = getOutlinePoints(loc, this.letterWidth * 0.0833, this.letterHeight * 0.0633, this.outlineWidth + 1);
			for(Point p : points) {
				g.drawImage(characterImages[this.text.charAt(index)].getScaledCopy(this.letterWidth, this.letterHeight), (float)p.getX(), (float)p.getY(), outlineColor);
			}
			*/
			
			g.drawImage(characterImage, (float)(loc.getX() + this.letterWidth * 0.1667), (float)(loc.getY()), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() + this.letterWidth * 0.0833), (float)(loc.getY() + this.letterHeight * 0.0633), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() + this.letterHeight * 0.1266), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() - this.letterWidth * 0.0833), (float)(loc.getY() + this.letterHeight * 0.0633), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() - this.letterWidth * 0.1667), (float)(loc.getY()), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() - this.letterWidth * 0.0833), (float)(loc.getY() - this.letterHeight * 0.0633), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() - this.letterHeight * 0.1266), outlineColor);
			g.drawImage(characterImage, (float)(loc.getX() + this.letterWidth * 0.0833), (float)(loc.getY() - this.letterHeight * 0.0633), outlineColor);
			/*
			if(this.getMap() != null) {
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() + this.letterWidth * 0.1667, loc.getY(), this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() + this.letterWidth * 0.0833, loc.getY() + this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() + this.letterHeight * 0.1266, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() - this.letterWidth * 0.0833, loc.getY() + this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() - this.letterWidth * 0.1667, loc.getY(), this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() - this.letterWidth * 0.0833, loc.getY() - this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() - this.letterHeight * 0.1266, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() + this.letterWidth * 0.0833, loc.getY() - this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, outlineColor.getRedByte(), outlineColor.getGreenByte(), outlineColor.getBlueByte(), outlineColor.getAlphaByte(), 0));
			}
			*/
		}
		g.drawImage(characterImage, (float)loc.getX(), (float)loc.getY(), color);
		if(this.getMap() != null) {
			//this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY(), this.letterWidth, this.letterHeight, 0, color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte(), 0));
		}
		if(isUppercase) {
			//boldens the text
			g.drawImage(characterImage, (float)(loc.getX() + this.letterWidth * 0.0833), (float)(loc.getY()), color);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() + this.letterHeight * 0.0633), color);
			g.drawImage(characterImage, (float)(loc.getX() - this.letterWidth * 0.0833), (float)(loc.getY()), color);
			g.drawImage(characterImage, (float)(loc.getX()), (float)(loc.getY() - this.letterHeight * 0.0633), color);
			/*
			if(this.getMap() != null) {
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() + this.letterWidth * 0.0833, loc.getY(), this.letterWidth, this.letterHeight, 0, color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() + this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX() - this.letterWidth * 0.0833, loc.getY(), this.letterWidth, this.letterHeight, 0, color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte(), 0));
				this.getMap().addToDrawInfo(GameMap.getDrawDataFN(characterImagePath, loc.getX(), loc.getY() - this.letterHeight * 0.0633, this.letterWidth, this.letterHeight, 0, color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte(), 0));
			}
			*/
		}
	}
	/*
	 * Pretty much obsolete
	 */
	public ArrayList<Point> getOutlinePoints(Point start, double xscale, double yscale, int width) {
		ArrayList<Point> points = new ArrayList<Point>();
		ArrayList<Point> proximityPoints = Point.proximity4(start, xscale, yscale);
		if(width > 0) {
			if(width == 1) {
				points.addAll(proximityPoints);
			}
			for(Point p : proximityPoints) {
				points.addAll(getOutlinePoints(p, xscale, yscale, width - 1));
			}
		}
		return points;
	}
	public Image getImage(String image) {
		Image returnImage;
		try {
			returnImage = new Image(image, false, Image.FILTER_NEAREST);
			return returnImage;
		} catch (SlickException e) {
			System.out.println("TEXT FAILURE" + image + "NOT FOUND");
			e.printStackTrace();
		}
		return null;
	}
	public Image getImageForCharacter(Character c) {
		return characterImages[c];
	}
	public String getImagePathFromCharacter(Character c) {
		switch(c) {
		case ' ':
			return "res/blank.png";
		case '|':
			return "res/blank.png";
		case '\'':
			return "res/ui/text/text_apostrophe.png";
		case ':':
			return "res/ui/text/text_colon.png";
		case ',':
			return "res/ui/text/text_comma.png";
		case '-':
			return "res/ui/text/text_dash.png";
		case '!':
			return "res/ui/text/text_exclam.png";
		case '(':
			return "res/ui/text/text_leftparen.png";
		case '%':
			return "res/ui/text/text_percent.png";
		case '.':
			return "res/ui/text/text_period.png";
		case '+':
			return "res/ui/text/text_plus.png";
		case '?':
			return "res/ui/text/text_question.png";
		case ')':
			return "res/ui/text/text_rightparen.png";
		case ';':
			return "res/ui/text/text_semicolon.png";
		case '/':
			return "res/ui/text/text_slash.png";
		default:
		return "res/ui/text/text_" + Character.toUpperCase(c) + ".png";
		}
	}
	public Image loadImageFromCharacter(Character c){
		switch(c) {
		case ' ':
			return getImage("res/blank.png");
		case '|':
			return getImage("res/blank.png");
		case '\'':
			return getImage("res/ui/text/text_apostrophe.png");
		case ':':
			return getImage("res/ui/text/text_colon.png");
		case ',':
			return getImage("res/ui/text/text_comma.png");
		case '-':
			return getImage("res/ui/text/text_dash.png");
		case '!':
			return getImage("res/ui/text/text_exclam.png");
		case '(':
			return getImage("res/ui/text/text_leftparen.png");
		case '%':
			return getImage("res/ui/text/text_percent.png");
		case '.':
			return getImage("res/ui/text/text_period.png");
		case '+':
			return getImage("res/ui/text/text_plus.png");
		case '?':
			return getImage("res/ui/text/text_question.png");
		case ')':
			return getImage("res/ui/text/text_rightparen.png");
		case ';':
			return getImage("res/ui/text/text_semicolon.png");
		case '/':
			return getImage("res/ui/text/text_slash.png");
		default:
		return getImage("res/ui/text/text_" + Character.toUpperCase(c) + ".png");
		}
	}
	public void setUseOutline(boolean state) {
		this.useOutline = state;
	}
	public void setOutlineColor(Color color) {
		this.outlineColor = color;
	}
	public void setText(String text) {
		if(this.text == text) {
			return; //IF THE TEXT IS THE SAME, DO NOTHING
		}
		this.text = text;
	}
	public void addText(String text) {
		this.text += text;
	}
	public void setLetterWidth(int width) {
		this.letterWidth = width;
	}
	public int getLetterWidth() {
		return this.letterWidth;
	}
	public void setLetterHeight(int height) {
		this.letterHeight = height;
	}
	public int getLetterHeight() {
		return this.letterHeight;
	}
	public int getLettersPerLine() {
		return (int)((this.boxWidth - this.letterWidth) / this.kerning);
	}
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	public void setKerning(int kerning) {
		this.kerning = kerning;
	}
	public void setScale(float scale) {
		this.letterWidth = (int)(scale * 6);
		this.letterHeight = (int)(scale * 8);
		this.kerning = (int)(scale * 7);
		this.lineSpacing = (int)(scale * 10);
	}
	public void setColor(Color color) {
		this.color = color;
	}
	private static boolean isUsableSpecialCharacter(int ch) {
		for(int i = 0; i < USABLE_SPECIAL_CHARACTERS.length; i++) {
			if(USABLE_SPECIAL_CHARACTERS[i] == (char)ch) {
				return true;
			}
		}
		return false;
	}
	@Override
	public float getWidth() {
		if(this.lines == 1) {
			return this.text.trim().length() * this.kerning;
		} else {
			return this.boxWidth;
		}
	}
	public int getBoxWidth() {
		return this.boxWidth;
	}
	@Override
	public float getHeight() {
		return this.lines * this.lineSpacing;
	}
	public void updateNumOfLines() {
		int currWordIndex = 0;
		int numLines = 0;
		float tempx = 0;
		ArrayList<String> textLines = new ArrayList<String>();
		String addbuffer = "";
		for(int i = 0; i < this.text.length(); i++) {
			if(this.text.charAt(i) == ' ' || i == this.text.length() - 1) {
				if(tempx > this.boxWidth || (i == this.text.length() - 1 && tempx + this.letterWidth > this.boxWidth)) {
					tempx = -kerning; //TO BALANCE OUT THE SPACE
					textLines.add(addbuffer);
					addbuffer = "";
					numLines++;
				} else {
					tempx += -kerning * (i - currWordIndex); //go back to start of word's position
				}
				for(int j = currWordIndex; j < i; j++) {
					addbuffer += this.text.charAt(j);
					tempx += kerning;
				}
				if(i == this.text.length() - 1) {
					addbuffer += this.text.charAt(i);
					textLines.add(addbuffer);
					addbuffer = "";
					numLines++;
				}
				currWordIndex = i;
				tempx += kerning;
			} else if(this.text.charAt(i) == NEW_LINE) {
				if(tempx > this.boxWidth) {
					tempx = -kerning;
					textLines.add(addbuffer);
					addbuffer = "";
					numLines++;
				} else {
					tempx += -kerning * (i - currWordIndex); //go back to start of word's position
				}
				for(int j = currWordIndex; j < i; j++) {
					addbuffer += this.text.charAt(j);
					tempx += kerning;
				}
				currWordIndex = i;
				tempx = 0;
				textLines.add(addbuffer);
				addbuffer = "";
				numLines++;
			} else {
				tempx += kerning;
			}
		}
		this.lines = numLines;
	}
}
