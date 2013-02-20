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
 * 地图引擎类
 * @author Administrator
 *
 */
public class MapEngine {
	
	//辅助的一维数组
	private int[] array;
	
	//存放返回路径的点的集合
	private List<Point> pointList = new ArrayList<Point>();
	//地图
	private int[][] map;
	
	/**
	 * 初始化地图
	 */
	public void initMap(int[][] map) {
		array = new int[ROWSIZE * COLUMNSIZE];
		//初始化辅助的一维数组
		initAugArray(array);
		//将一维数组内容拷贝到二维数组中
		setMap(map, array);
		if (!isHaveMatchElement(map)) {
			Log.i("初始化地图", "发现地图不可连通，重新初始化地图");
			initMap(map);
		}
	}
	
	/**
	 * 对辅助的一维数组进行初始化
	 * @param array
	 */
	private void initAugArray(int[] array) {
		//每种图片的数目
		int averageSize = (ROWSIZE * COLUMNSIZE) / CURRENTIMAGECOUNT;
		//如果每种图片的数目为奇数，将其减一，保证为偶数
		if (averageSize % 2 == 1) {
			averageSize--;
		}
		//按照顺序分配到一维数组中
		for (int i = 0; i < CURRENTIMAGECOUNT; i++) {
			for (int j = 0; j < averageSize; j++) {
				array[i * averageSize + j] = i + 1;
			}
		}
		//对一维数组中剩下没有进行赋值的用随机数进行赋值
		int leaveCount = (ROWSIZE * COLUMNSIZE) - CURRENTIMAGECOUNT * averageSize;
		Random random = new Random();
		if (leaveCount > 0) {
			for (int i = 0; i < leaveCount / 2; i++) {
				int num = random.nextInt(CURRENTIMAGECOUNT - 1);
				array[ROWSIZE * COLUMNSIZE - i * 2 - 1] = num + 1;
				array[ROWSIZE * COLUMNSIZE - i * 2 - 2] = num + 1;
			}
		}
		//Debug用
		System.out.println("辅助一维数组的内容为：");
		String str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println(str);
		
		//将数组中的数值打乱
		for (int i=array.length; i > 0; i--) {
			int j = random.nextInt(i);
			int temp = array[j];
			array[j] = array[i - 1];
			array[i - 1] = temp;
		}
		//Debug用
		System.out.println("打乱之后的辅助数组为：");		
		str = "";
		for (int i=0; i<array.length; i++) {
			str = str + array[i] + ",";
		}
		System.out.println(str);
	}
	
	/**
	 * 将一维数组内容拷贝到二维数组中
	 * @param map
	 * @param array
	 */
	private void setMap(int[][] map, int[] array) {
		//先将数组中的内容设置为0
		for (int i=0; i<(ROWSIZE + 2); i++) {
			for (int j=0; j<(COLUMNSIZE + 2); j++) {
				map[i][j] = 0;
			}
		}
		//将一维数组中的数值复制到二维数组中
		int count = 0;
		for (int i=1; i<=ROWSIZE; i++) {
			for (int j=1; j<=COLUMNSIZE; j++) {
				map[i][j] = array[count];
				count++;
			}
		}
		//debug用
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
	 * 本类提供的对外api，负责查找路径，并返回路径返回
	 * @param map	地图
	 * @param start	开始点
	 * @param end	结束点
	 * @return	返回路径上点的集合，如果没有找到路径，返回空集合
	 */
	public List<Point> searchRoute(int[][] map, Point start, Point end) {
		this.map = map;
		pointList.clear();
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
		if (end.x != start.x)
			return false;
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
	 * 检测是否还有可以匹配的元素
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
	 * 查找map中第一个和point的值匹配的点
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
	 * 刷新地图，将地图的二维数组中的不为0的内容放入到一维数组中，然后更新一维数组中的顺序
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
		//检测如果没有可以匹配的元素重新分配地图
		if (isHaveMatchElement(map) == false) {
			refreshMap(map);
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
	
	/**
	 * 根据游戏等级更新地图
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
		//如果方块是上下移动
		if (mode == 1 || mode == 2) {
			int maxY = curPoint.y >= endPoint.y ? curPoint.y : endPoint.y;
			Point maxPoint = curPoint.y >= endPoint.y ? curPoint : endPoint;
			int minY = curPoint.y < endPoint.y ? curPoint.y : endPoint.y;
			Point minPoint = curPoint.y < endPoint.y ? curPoint : endPoint;
			//方块消除时其他方块向上移动
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
			//方块消除时其他方块向下移动
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
		//如果方块是左右移动
		if (mode == 3 || mode == 4) {
			int maxX = curPoint.x >= endPoint.x ? curPoint.x : endPoint.x;
			Point maxPoint = curPoint.x >= endPoint.x ? curPoint : endPoint;
			int minX = curPoint.x < endPoint.x ? curPoint.x : endPoint.x;
			Point minPoint = curPoint.x < endPoint.x ? curPoint : endPoint;
			//方块消除时其他方块向左移动
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
			//方块消除时其他方块向右移动
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
		//再次判断集合中值为0的点并将其删除
		if (pointSet != null && pointSet.isEmpty() == false) {
			String str = "";
			for (Iterator<Point> iterator = pointSet.iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				str += "(" + point.y + "," + point.x + "),";
			}
			Log.i("将要发送的点的集合为", str);
			
			for (Iterator<Point> iterator = pointSet.iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				if (map[point.y][point.x] == 0) {
					Log.i("删除点", "(" + point.y + "," + point.x + ")");
					iterator.remove();
				}
			}
		}
		return pointSet;
	}
	
	/**
	 * 检测地图是否为空
	 * @param map
	 * @return
	 */
	public static boolean isMapEmpty(int[][] map, int row, int colum) {
		//debug用
		Log.i("地图内容为", "地图内容");
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
