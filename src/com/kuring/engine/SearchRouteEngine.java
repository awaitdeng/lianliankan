package com.kuring.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.graphics.Point;
import static com.kuring.util.ConstantUtil.*;

/**
 * ����·�����棬������������·��
 * @author Administrator
 *
 */
public class SearchRouteEngine {
	
	//��ŷ���·���ĵ�ļ���
	private List<Point> pointList = new ArrayList<Point>();
	//��ͼ
	private int[][] map;
	
	/**
	 * �����ṩ�Ķ���api���������·����������·������
	 * @param map	��ͼ
	 * @param start	��ʼ��
	 * @param end	������
	 * @return	����·���ϵ�ļ��ϣ����û���ҵ�·�������ؿռ���
	 */
	public List<Point> searchRoute(int[][] map, Point start, Point end) {
		this.map = map;
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
		if (start.x != end.x) {
			return false;
		}
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
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				Point point = findOneElement(map[i][j], new Point(j, i));
				if (null != point) {
					return searchRoute(map, new Point(j, i), point);
				}
			}
		}
		return null;
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
				if (map[i][j] == value && point.y == i && point.x == j) {
					return new Point(j, i);
				}
			}
		}
		return null;
	}
	
	/**
	 * ˢ�µ�ͼ������ͼ�Ķ�ά�����еĲ�Ϊ0�����ݷ��뵽һά�����У�Ȼ�����һλ�����е�˳��
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
	
}
