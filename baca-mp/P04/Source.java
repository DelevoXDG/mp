// Maksim Zdobnikau - 2

import java.util.Scanner;

class Car {
	private String _name;
	private Car _next;
	private Car _prev;

	public Car(String name, Car prev, Car next) {
		this._name = name;
		this._next = next;
		this._prev = prev;
	}
	public Car(String name) {
		this._name = name;
		this._next = this;
		this._prev = this;
	}
	public String getName() {
		return _name;
	}
	public Car getPrev() {
		return _prev;
	}
	public Car getNext() {
		return _next;
	}
	public void hookNext(Car next) {
		_next = next;
	}
	public void hookPrev(Car prev) {
		_prev = prev;
	}

}

class Train {
	private String _name;
	private Train _next;
	private Car _first;

	public Train(String trainName, String carName, Train next) {
		this._name = trainName;
		this._first = new Car(carName);
		this._next = next;
	}
	public Train(String trainName) {
		this._name = trainName;
	}
	public boolean isEqual(String trainName) {
		if (this._name.equals(trainName)) {
			return true;
		}
		return false;
	}
	public String getName() {
		return _name;
	}
	public Train getNext() {
		return _next;
	}
	public void hookNext(Train next) {
		_next = next;
	}
	public String getCars() {
		String	carListString	= "";
		Car		curCar			= this._first;
		Car		prevCar			= null;
		do {
			carListString += " " + curCar.getName();
			if (curCar.getNext() == prevCar) {
				prevCar = curCar;
				curCar = curCar.getPrev();
			} else {
				prevCar = curCar;
				curCar = curCar.getNext();
			}
		} while (curCar != this._first);

		return carListString;
	}
	public Car getLast() {
		return this._first.getPrev();
	}
	public void insertLastCar(String carName) {
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
	}
	public void reverse() {
		_first = getLast();
		_first.hookNext(null);
	}
}

// Section List
class TrainList {
	private Train _trains;

	public TrainList() {
		this._trains = null;
	}
	public TrainList(Train train) {
		this._trains = train;
	}
	public Train findTrain(String trainName) {
		for (Train curTrain = this._trains; curTrain != null; curTrain = curTrain.getNext()) {
			if (curTrain.isEqual(trainName)) {
				return curTrain;
			}
		}
		return null;
	}

	public void newTrain(String trainName, String carName) {
		// Wstawenie pociagu na poczatek listy, zeby spelnic zlozonosc O(1)
		Train curTrain = this.findTrain(trainName);

		if (curTrain == null) {
			curTrain = new Train(trainName, carName, this._trains);
			this._trains = curTrain;
		} else {
			Error.printError(Error.trainExists, trainName);
		}
	}
	public void insertLast(String trainName, String carName) {
		Train curTrain = findTrain(trainName);

		if (curTrain != null) {
			curTrain.insertLastCar(carName);
		} else {
			Error.printError(Error.trainNotExists, trainName);
		}
	}

	public void printCarList(String trainName) {
		String	carList		= "";
		Train	curTrain	= findTrain(trainName);
		if (curTrain != null) {
			carList += curTrain.getName() + ":" + curTrain.getCars();
			System.out.println(carList);
		} else {
			Error.printError(Error.trainNotExists, trainName);
		}
	}
	public void Reverse(String trainName) {
		Train curTrain = findTrain(trainName);
		if (curTrain != null) {
			curTrain.reverse();
		} else {
			Error.printError(Error.trainNotExists, trainName);
		}
	}
}

enum Error {
	trainExists,
	trainNotExists;

	public static void printError(Error errName, String trainName) {
		switch (errName) {
		case trainExists:
			System.out.println("Train " + trainName + " alerady exists");
			break;
		case trainNotExists:
			System.out.println("Train " + trainName + " does not exist");
			break;
		default:
			// nothing to print
			break;
		}
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int test_count = 0;
		test_count = sc.nextInt();
		// TrainStation station = new TrainStation();
		TrainList trainList = new TrainList();
		while (test_count-- > 0) {
			int OP_count = sc.nextInt();
			while (OP_count-- > 0) {
				String OP = sc.next();
				switch (OP) {
				case "New": {
					String	trainName	= sc.next();
					String	carName		= sc.next();

					trainList.newTrain(trainName, carName);
					break;
				}
				case "InsertFirst": {
					String	trainName	= sc.next();
					String	carName		= sc.next();

					// trainList.insertFirst(trainName, carName);
					break;
				}
				case "InsertLast": {
					String	trainName	= sc.next();
					String	carName		= sc.next();

					trainList.insertLast(trainName, carName);
					break;
				}
				case "Display": {
					String trainName = sc.next();
					trainList.printCarList(trainName);
					break;
				}
				case "Trains": {
					break;
				}
				case "Reverse": {
					String trainName = sc.next();
					trainList.Reverse(trainName);
					break;
				}
				case "Union": {
					break;
				}
				case "DelFirst": {
					break;
				}
				case "DelLast":
					break;
				}
			}
		}
	}
}
