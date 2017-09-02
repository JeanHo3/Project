import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class CoreControl extends JPanel {

        /**
	 * 
	 */
	private static final long serialVersionUID = 4812186509260752451L;
		private List<Point> fillCells;
		private List<Color> cellsColors;

        public CoreControl() {
            fillCells = new ArrayList<>(100);
            cellsColors = new ArrayList<>(100);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i=0;i<fillCells.size();i++) {
                int cellX = (fillCells.get(i).x * 5);
                int cellY = (fillCells.get(i).y * 5);
                g.setColor(cellsColors.get(i));
                g.fillRect(cellX, cellY, 5, 5);
            }
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 970, 450);

            for (int i = 0; i <= 970; i += 5) {
                g.drawLine(i, 0, i, 450);
            }

            for (int i = 0; i <= 450; i += 5) {
                g.drawLine(0, i, 970, i);
            }
        }

        public void fillCell(int x, int y, Color color) {
            fillCells.add(new Point(x, y));
            cellsColors.add(color);
            repaint();
        }

    }
