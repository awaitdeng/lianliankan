package com.kuring.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.graphics.Point;
import static com.kuring.util.ConstantUtil.*;

/**
 * 查找路径引擎，用来搜索查找路径
 * @author Administrator
 *
 */
public class SearchRouteEngine {
	
	//存放返回路径的点的集合
	private List<Point> pointList = new ArrayList<Point>();
	//地图
	private int[][] map;
	
	/**
	 * 本类提供的对外api，负责查找路径，并返回路径返回
	 * @param map	地图
	 * @param start	开始点
	 * @param end	结束点
	 * @return	返回路径上点的集合，如果没有找到路径，返回空集合
	 */
	public List<Point> searchRoute(int[][] map, Point start, Point end) {
		this.map = map;
		if (horizon(start, end)) {
			System.out.println("检测到两点可以通过水平方向直接连通");
			addPointToList(start);
			addPointToList(end);
		} else if (vertical(start, end)) {
			System.out.println("检测到两点可以通过垂直方向直接连通");
			addPointToList(start);
			addPointToList(end);
		} else if (oneCorner(start, end)) {
			System.out.println("检测到两点可以通过一个拐点连通");
		} else if (twoCornor(start, end)) {
			System.out.println("检测到两点可以通过两个拐点连通");
		}
		return pointList;
	}
	
	/**
	 * 水平方向是否可以直接联通
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean horizon(Point start, Point end) {
		//判断两个点是否在水平的一条直线上
		if (start.y != end.y) {
			return false;
		}
		//判断两个点是否是同一个点
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
	 * 垂直方向是否可以直接联通
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean vertical(Point start, Point end) {
		//判断两个点是否在垂直的一条直线上
		if (start.x != end.x) {
			return false;
		}
		//判断两个点是否是同一个点
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
	 * 一个拐角的检测
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean oneCorner(Point start, Point end) {
		Point c = new Point(start.x, end.y);	//c点和b点在同一水平线上
		Point d = new Point(end.x, start.y);	//d点和a点在同一水平线上
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
	 * 两个拐角的检测
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean twoCornor(Point start, Point end) {
		//扫描start左边所有的点
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
		//扫描start右边所有的点
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
		//扫描start上边所有的点
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
		//扫描start下边所有的点
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
	 * 根据地图自动查找可匹配的元素
	 * 从地图的左上角开始查找，如果发现可匹配的元素，将其加入到点的集合中
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
	 * 查找map中第一个和point的值匹配的点
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
	 * 刷新地图，将地图的二维数组中的不为0的内容放入到一维数组中，然后更新一位数组中的顺序
	 * 如果地图为空，即不需要刷新，返回false
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
		//将一维数组中的内容打乱
		Random random = new Random();
		for (int i=count; i>0; i--) {
			int j = random.nextInt(i);
			int temp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = temp;
		}
		//Debug用
		String str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println("打乱的辅助一维数组为" + str);
		//将一维数组中的内容重新赋值给二维map数组
		count = 0;
		for (int i=1; i<=ROWSIZE + 1; i++) {
			for (int j=1; j<=COLUMNSIZE + 1; j++) {
				if (map[i][j] != 0) {
					 map[i][j] = array[count];
					count++;
				}
			}
		} 
		//Debug用
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
	 * 增加点到ArrayList中，在加入之前首先检测改点是否已经存在集合中，如果存在则不加入
	 * @param point
	 */
	private void addPointToList(Point point) {
		if (!pointList.contains(point)) {
			pointList.add(point);
		}
	}
	
}
