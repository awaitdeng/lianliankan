package com.kuring.engine;

import static com.kuring.util.ConstantUtil.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.kuring.entity.Level;

import android.graphics.Point;
import android.util.Log;

/**
 * ��ͼ������
 * @author Administrator
 *
 */
public class MapEngine {
	
	//������һά����
	private int[] array;
	
	//��ŷ���·���ĵ�ļ���
	private List<Point> pointList = new ArrayList<Point>();
	//��ͼ
	private int[][] map;
	
	/**
	 * ��ʼ����ͼ
	 */
	public void initMap(int[][] map) {
		array = new int[ROWSIZE * COLUMNSIZE];
		//��ʼ��������һά����
		initAugArray(array);
		//��һά�������ݿ�������ά������
		setMap(map, array);
		if (!isHaveMatchElement(map)) {
			Log.i("��ʼ����ͼ", "���ֵ�ͼ������ͨ�����³�ʼ����ͼ");
			initMap(map);
		}
	}
	
	/**
	 * �Ը�����һά������г�ʼ��
	 * @param array
	 */
	private void initAugArray(int[] array) {
		//ÿ��ͼƬ����Ŀ
		int averageSize = (ROWSIZE * COLUMNSIZE) / CURRENTIMAGECOUNT;
		//���ÿ��ͼƬ����ĿΪ�����������һ����֤Ϊż��
		if (averageSize % 2 == 1) {
			averageSize--;
		}
		//����˳����䵽һά������
		for (int i = 0; i < CURRENTIMAGECOUNT; i++) {
			for (int j = 0; j < averageSize; j++) {
				array[i * averageSize + j] = i + 1;
			}
		}
		//��һά������ʣ��û�н��и�ֵ������������и�ֵ
		int leaveCount = (ROWSIZE * COLUMNSIZE) - CURRENTIMAGECOUNT * averageSize;
		Random random = new Random();
		if (leaveCount > 0) {
			for (int i = 0; i < leaveCount / 2; i++) {
				int num = random.nextInt(CURRENTIMAGECOUNT - 1);
				array[ROWSIZE * COLUMNSIZE - i * 2 - 1] = num + 1;
				array[ROWSIZE * COLUMNSIZE - i * 2 - 2] = num + 1;
			}
		}
		//Debug��
		System.out.println("����һά���������Ϊ��");
		String str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println(str);
		
		//�������е���ֵ����
		for (int i=array.length; i > 0; i--) {
			int j = random.nextInt(i);
			int temp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = temp;
		}
		//Debug��
		System.out.println("����֮��ĸ�������Ϊ��");		
		str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println(str);
	}
	
	/**
	 * ��һά�������ݿ�������ά������
	 * @param map
	 * @param array
	 */
	private void setMap(int[][] map, int[] array) {
		//�Ƚ������е���������Ϊ0
		for (int i=0; i<(ROWSIZE + 2); i++) {
			for (int j=0; j<(COLUMNSIZE + 2); j++) {
				map[i][j] = 0;
			}
		}
		//��һά�����е���ֵ���Ƶ���ά������
		int count = 0;
		for (int i=1; i<=ROWSIZE; i++) {
			for (int j=1; j<=COLUMNSIZE; j++) {
				map[i][j] = array[count];
				count++;
			}
		}
		//debug��
		String str = "";
		for (int i=0; i<(ROWSIZE + 2); i++) {
			for (int j=0; j<(COLUMNSIZE + 2); j++) {
				str = str + map[i][j] + ",";
			}
			System.out.println(str);
			str = "";
		}
	}
	
	/**
	 * �����ṩ�Ķ���api���������·����������·������
	 * @param map	��ͼ
	 * @param start	��ʼ��
	 * @param end	������
	 * @return	����·���ϵ�ļ��ϣ����û���ҵ�·�������ؿռ���
	 */
	public List<Point> searchRoute(int[][] map, Point start, Point end) {
		this.map = map;
		pointList.clear();
		if (horizon(start, end)) {
			System.out.println("��⵽�������ͨ��ˮƽ����ֱ����ͨ");
			addPointToList(start);
			addPointToList(end);
		} else if (vertical(start, end)) {
			System.out.println("��⵽�������ͨ����ֱ����ֱ����ͨ");
			addPointToList(start);
			addPointToList(end);
		} else if (oneCorner(start, end)) {
			System.out.println("��⵽�������ͨ��һ���յ���ͨ");
		} else if (twoCornor(start, end)) {
			System.out.println("��⵽�������ͨ�������յ���ͨ");
		}
		return pointList;
	}
	
	/**
	 * ˮƽ�����Ƿ����ֱ����ͨ
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean horizon(Point start, Point end) {
		//�ж��������Ƿ���ˮƽ��һ��ֱ����
		if (start.y != end.y) {
			return false;
		}
		//�ж��������Ƿ���ͬһ����
		if (start.x == end.x && start.y == end.y) {
			return false;
		}
		int xStart = (start.x <= end.x ? start.x : end.x);
		int xEnd = (start.x <= end.x ? end.x : start.x);
		for (int x = xStart + 1; x < xEnd; x++) {
			if (map[start.y][x] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ��ֱ�����Ƿ����ֱ����ͨ
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean vertical(Point start, Point end) {
		//�ж��������Ƿ��ڴ�ֱ��һ��ֱ����
		if (end.x != start.x)
			return false;
		//�ж��������Ƿ���ͬһ����
		if (start.x == end.x && start.y == end.y) {
			return false;
		}
		int yStart = (start.y <= end.y ? start.y : end.y);
		int yEnd = (start.y <= end.y ? end.y : start.y);
		for (int y = yStart + 1; y < yEnd; y++) {
			if (map[y][start.x] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * һ���սǵļ��
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean oneCorner(Point start, Point end) {
		Point c = new Point(start.x, end.y);	//c���b����ͬһˮƽ����
		Point d = new Point(end.x, start.y);	//d���a����ͬһˮƽ����
		boolean cResult = false;
		boolean dResult = false;
		if (map[c.y][c.x] == 0)	{
			cResult = (horizon(c, end) && vertical(c, start));
		}
		if (map[d.y][d.x] == 0) {
			dResult = (horizon(d, start) && vertical(d, end));
		}
		if (cResult || dResult) {
			addPointToList(start);
			if (cResult) {
				addPointToList(c);
			} else {
				addPointToList(d);
			}
			addPointToList(end);
			return true;
		}
		pointList.clear();
		return false;
	}
	
	/**
	 * �����սǵļ��
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean twoCornor(Point start, Point end) {
		//ɨ��start������еĵ�
		for (int x = (start.x - 1); x >= 0; x--) {
			if (map[start.y][x] == 0 
				&& map[end.y][x] == 0 
				&& vertical(new Point(x, start.y), new Point(x, end.y))
				&& horizon(start, new Point(x, start.y))
				&& horizon(end, new Point(x, end.y))) {
				addPointToList(start);
				addPointToList(new Point(x, start.y));
				addPointToList(new Point(x, end.y));
				addPointToList(end);
				return true;
			}
		}
		//ɨ��start�ұ����еĵ�
		for (int x = (start.x + 1); x <= COLUMNSIZE + 1; x++) {
			if (map[start.y][x] == 0 
				&& map[end.y][x] == 0 
				&& vertical(new Point(x, start.y), new Point(x, end.y))
				&& horizon(start, new Point(x, start.y))
				&& horizon(end, new Point(x, end.y))) {
				addPointToList(start);
				addPointToList(new Point(x, start.y));
				addPointToList(new Point(x, end.y));
				addPointToList(end);
				return true;
			}
		}
		//ɨ��start�ϱ����еĵ�
		for (int y = (start.y - 1); y >= 0; y--) {
			if (map[y][start.x] == 0 
				&& map[y][end.x] == 0 
				&& horizon(new Point(start.x, y), new Point(end.x, y))
				&& vertical(start, new Point(start.x, y))
				&& vertical(end, new Point(end.x, y))) {
				addPointToList(start);
				addPointToList(new Point(start.x, y));
				addPointToList(new Point(end.x, y));
				addPointToList(end);
				return true;
			}
		}
		//ɨ��start�±����еĵ�
		for (int y =(start.y + 1); y <= ROWSIZE + 1; y++) {
			if (map[y][start.x] == 0 
				&& map[y][end.x] == 0 
				&& horizon(new Point(start.x, y), new Point(end.x, y))
				&& vertical(start, new Point(start.x, y))
				&& vertical(end, new Point(end.x, y))) {
				addPointToList(start);
				addPointToList(new Point(start.x, y));
				addPointToList(new Point(end.x, y));
				addPointToList(end);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ���ݵ�ͼ�Զ����ҿ�ƥ���Ԫ��
	 * �ӵ�ͼ�����Ͻǿ�ʼ���ң�������ֿ�ƥ���Ԫ�أ�������뵽��ļ�����
	 * @return
	 */
	public List<Point> findMatchElement(int[][] map) {
		pointList.clear();
		this.map = map;
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				if (map[i][j] != 0) {
					Point point = findOneElement(map[i][j], new Point(j, i));
					if (null != point) {
						searchRoute(map, new Point(j, i), point);
						if (pointList != null && pointList.size() > 0) {
							return pointList;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * ����Ƿ��п���ƥ���Ԫ��
	 * @param map
	 * @return
	 */
	public boolean isHaveMatchElement(int[][] map) {
		findMatchElement(map);
		if (pointList != null && pointList.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * ����map�е�һ����point��ֵƥ��ĵ�
	 * @param value
	 * @param point
	 * @return
	 */
	private Point findOneElement(int value, Point point) {
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				if (map[i][j] == value) {
					if (point.y == i && point.x == j) {
						continue;
					}
					return new Point(j, i);
				}
			}
		}
		return null;
	}
	
	/**
	 * ˢ�µ�ͼ������ͼ�Ķ�ά�����еĲ�Ϊ0�����ݷ��뵽һά�����У�Ȼ�����һά�����е�˳��
	 * �����ͼΪ�գ�������Ҫˢ�£�����false
	 * @param map
	 * @return
	 */
	public boolean refreshMap(int[][] map) {
		int[] array = new int[COLUMNSIZE * ROWSIZE];
		int count = 0;
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				if (map[i][j] != 0) {
					array[count] = map[i][j];
					count++;
				}
			}
		}
		if (count == 0) {
			return false;
		}
		//��һά�����е����ݴ���
		Random random = new Random();
		for (int i=count; i>0; i--) {
			int j = random.nextInt(i);
			int temp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = temp;
		}
		//Debug��
		String str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println("���ҵĸ���һά����Ϊ" + str);
		//��һά�����е��������¸�ֵ����άmap����
		count = 0;
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				if (map[i][j] != 0) {
					 map[i][j] = array[count];
					count++;
				}
			}
		} 
		//Debug��
		str = "";
		for (int i=0; i<(ROWSIZE + 2); i++) {
			for (int j=0; j<(COLUMNSIZE + 2); j++) {
				str = str + map[i][j] + ",";
			}
			System.out.println(str);
			str = "";
		}
		//������û�п���ƥ���Ԫ�����·����ͼ
		if (isHaveMatchElement(map) == false) {
			refreshMap(map);
		}
		return true;
	}
	
	/**
	 * ���ӵ㵽ArrayList�У��ڼ���֮ǰ���ȼ��ĵ��Ƿ��Ѿ����ڼ����У���������򲻼���
	 * @param point
	 */
	private void addPointToList(Point point) {
		if (!pointList.contains(point)) {
			pointList.add(point);
		}
	}
	
	/**
	 * ������Ϸ�ȼ����µ�ͼ
	 * @param map
	 * @param level
	 * @param curPoint
	 * @param endPoint
	 */
	public Set<Point> update(int[][] map, Level level, Point curPoint, Point endPoint) {
		Set<Point> pointSet = new HashSet<Point>();
		map[curPoint.y][curPoint.x] = 0;
		map[endPoint.y][endPoint.x] = 0;
		if (isMapEmpty(map, ROWSIZE, COLUMNSIZE)) {
			return null;
		}
		int mode = level.getMode();
		if (mode == 0) {
			return null;
		}
		//��������������ƶ�
		if (mode == 1 || mode == 2) {
			int maxY = curPoint.y >= endPoint.y ? curPoint.y : endPoint.y;
			Point maxPoint = curPoint.y >= endPoint.y ? curPoint : endPoint;
			int minY = curPoint.y < endPoint.y ? curPoint.y : endPoint.y;
			Point minPoint = curPoint.y < endPoint.y ? curPoint : endPoint;
			//��������ʱ�������������ƶ�
			if (mode == 1) {
				for (int i=maxY; i<ROWSIZE+1; i++) {
					map[i][maxPoint.x] = map[i+1][maxPoint.x];
					if (map[i][maxPoint.x] != 0) {
						pointSet.add(new Point(maxPoint.x, i));
					}
				}
				for (int i=minY; i<ROWSIZE+1; i++) {
					map[i][minPoint.x] = map[i+1][minPoint.x];
					if (map[i][minPoint.x] != 0) {
						pointSet.add(new Point(minPoint.x, i));
					}
				}
			}
			//��������ʱ�������������ƶ�
			if (mode == 2) {
				for (int i=minY; i>0; i--) {
					map[i][minPoint.x] = map[i-1][minPoint.x];
					if (map[i][minPoint.x] != 0) {
						pointSet.add(new Point(minPoint.x, i));
					}
				}
				for (int i=maxY; i>0; i--) {
					map[i][maxPoint.x] = map[i-1][maxPoint.x];
					if (map[i][maxPoint.x] != 0) {
						pointSet.add(new Point(maxPoint.x, i));
					}
				}
			}
		}
		//��������������ƶ�
		if (mode == 3 || mode == 4) {
			int maxX = curPoint.x >= endPoint.x ? curPoint.x : endPoint.x;
			Point maxPoint = curPoint.x >= endPoint.x ? curPoint : endPoint;
			int minX = curPoint.x < endPoint.x ? curPoint.x : endPoint.x;
			Point minPoint = curPoint.x < endPoint.x ? curPoint : endPoint;
			//��������ʱ�������������ƶ�
			if (mode == 3) {
				for (int i=maxX; i<COLUMNSIZE+1; i++) {
					map[maxPoint.y][i] = map[maxPoint.y][i+1];
					if (map[maxPoint.y][i] != 0) {
						pointSet.add(new Point(i, maxPoint.y));
					}
				}
				for (int i=minX; i<COLUMNSIZE+1; i++) {
					map[minPoint.y][i] = map[minPoint.y][i+1];
					if (map[minPoint.y][i] != 0) {
						pointSet.add(new Point(i, minPoint.y));
					}
				}
			}
			//��������ʱ�������������ƶ�
			if (mode == 4) {
				for (int i=minX; i>0; i--) {
					map[minPoint.y][i] = map[minPoint.y][i-1];
					if (map[minPoint.y][i] != 0) {
						pointSet.add(new Point(i, minPoint.y));
					}
				}
				for (int i=maxX; i>0; i--) {
					map[maxPoint.y][i] = map[maxPoint.y][i-1];
					if (map[maxPoint.y][i] != 0) {
						pointSet.add(new Point(i, maxPoint.y));
					}
				}
			}
		}
		//�ٴ��жϼ�����ֵΪ0�ĵ㲢����ɾ��
		if (pointSet != null && pointSet.isEmpty() == false) {
			String str = "";
			for (Iterator<Point> iterator = pointSet.iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				str += "(" + point.y + "," + point.x + "),";
			}
			Log.i("��Ҫ���͵ĵ�ļ���Ϊ", str);
			
			for (Iterator<Point> iterator = pointSet.iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				if (map[point.y][point.x] == 0) {
					Log.i("ɾ����", "(" + point.y + "," + point.x + ")");
					iterator.remove();
				}
			}
		}
		return pointSet;
	}
	
	/**
	 * ����ͼ�Ƿ�Ϊ��
	 * @param map
	 * @return
	 */
	public static boolean isMapEmpty(int[][] map, int row, int colum) {
		//debug��
		Log.i("��ͼ����Ϊ", "��ͼ����");
		String str = "";
		for (int i=0; i<(ROWSIZE + 2); i++) {
			for (int j=0; j<(COLUMNSIZE + 2); j++) {
				str = str + map[i][j] + ",";
			}
			System.out.println(str);
			str = "";
		}
		for (int i=0; i<(row+2); i++) {
			for (int j=0; j<(colum+2); j++) {
				if (map[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}
}
