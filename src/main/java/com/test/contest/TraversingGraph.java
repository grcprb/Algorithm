package com.test.contest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;

public class TraversingGraph {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);
		String[] output = null;
		int ip1_size = 0;
		ip1_size = Integer.parseInt(in.nextLine().trim());
		String[] ip1 = new String[ip1_size];
		String ip1_item;
		for (int ip1_i = 0; ip1_i < ip1_size; ip1_i++) {
			try {
				ip1_item = in.nextLine();
			} catch (Exception e) {
				ip1_item = null;
			}
			ip1[ip1_i] = ip1_item;
		}
		int ip2_size = 0;
		ip2_size = Integer.parseInt(in.nextLine().trim());
		String[] ip2 = new String[ip2_size];
		String ip2_item;
		for (int ip2_i = 0; ip2_i < ip2_size; ip2_i++) {
			try {
				ip2_item = in.nextLine();
			} catch (Exception e) {
				ip2_item = null;
			}
			ip2[ip2_i] = ip2_item;
		}
		output = warehouseScalability(ip1, ip2);
		for (int output_i = 0; output_i < output.length; output_i++) {
			System.out.println(String.valueOf(output[output_i]));
		}
	}

	public static String[] warehouseScalability(String[] input1, String[] input2) {
		HashMap<String, TreeSet<Employee>> l_employeeMap = new HashMap<String, TreeSet<Employee>>();
		HashMap<String, TreeSet<Job>> l_jobMap = new HashMap<String, TreeSet<Job>>();

		ArrayList<String> l_outputArr = new ArrayList<String>();
		if (input1 != null) {
			for (int i = 0; i < input1.length; i++) {
				TreeSet<Employee> l_list;
				String[] split = input1[i].split("#");
				if (l_employeeMap.containsKey(split[1])) {
					l_list = l_employeeMap.get(split[1]);
					l_list.add(new Employee(split[0], split[1]));
				} else {
					l_list = new TreeSet<Employee>();
					l_list.add(new Employee(split[0], split[1]));
				}
				l_employeeMap.put(split[1], l_list);
			}

		}

		if (input2 != null) {
			for (int i = 0; i < input2.length; i++) {
				TreeSet<Job> l_list = new TreeSet<Job>();
				String l_temp2[] = input2[i].split("#");
				if (l_jobMap.containsKey(l_temp2[0]))
					l_list = l_jobMap.get(l_temp2[0]);
				l_list.add(new Job(Integer.parseInt(l_temp2[1]), Integer.parseInt(l_temp2[2]), l_temp2[3], l_temp2[0]));

				l_jobMap.put(l_temp2[0], l_list);
			}
		}

		for (Iterator<Entry<String, TreeSet<Employee>>> iterator = l_employeeMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, TreeSet<Employee>> pair = (Map.Entry<String, TreeSet<Employee>>) iterator.next();
			String l_key = (String) pair.getKey();
			TreeSet<Employee> l_empList = (TreeSet<Employee>) pair.getValue();
			System.out.println(l_key);
			for (Iterator iterator2 = l_empList.iterator(); iterator2.hasNext();) {
				Employee employee = (Employee) iterator2.next();
				System.out.println(employee);
			}
		}

		for (Iterator<Entry<String, TreeSet<Job>>> iterator = l_jobMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, TreeSet<Job>> pair = (Map.Entry<String, TreeSet<Job>>) iterator.next();
			String l_key = (String) pair.getKey();
			TreeSet<Job> l_jobList = (TreeSet<Job>) pair.getValue();
			System.out.println(l_key);
			for (Iterator iterator2 = l_jobList.iterator(); iterator2.hasNext();) {
				Job job = (Job) iterator2.next();
				System.out.println(job);
			}
		}

		Iterator<Entry<String, TreeSet<Job>>> l_itr = l_jobMap.entrySet().iterator();
		while (l_itr.hasNext()) {
			Map.Entry<String, TreeSet<Job>> pair = (Map.Entry<String, TreeSet<Job>>) l_itr.next();
			String l_key = (String) pair.getKey();
			TreeSet<Job> l_jobList = (TreeSet<Job>) pair.getValue();
			Iterator<Job> l_jobitr = l_jobList.iterator();
			while (l_jobitr.hasNext()) {
				Job l_job = (Job) l_jobitr.next();
				TreeSet<Employee> l_empList = l_employeeMap.get(l_key);
				if (l_empList.size() == 1) {
					Employee l_emp = l_empList.first();
					l_emp.jobList.add(l_job);
					l_emp.totalhours = l_emp.totalhours + l_job.time;
					// continue;
				} else {
					Employee l_emp = l_empList.first();
					l_empList.remove(l_emp);
					l_emp.jobList.add(l_job);
					l_emp.totalhours = l_emp.totalhours + l_job.time;
					l_empList.add(l_emp);
					System.out.println("-----------------------------------");
					for (Iterator iterator2 = l_empList.iterator(); iterator2.hasNext();) {
						Employee employee = (Employee) iterator2.next();
						System.out.println(employee);
					}
				}
			}
		}

		TreeSet<Employee> allEMP = new TreeSet<>(new Comparator<Employee>() {

			@Override
			public int compare(Employee o1, Employee o2) {
				return o1.designation.compareTo(o2.designation);
			}
		});
		for (Iterator<Entry<String, TreeSet<Employee>>> iterator = l_employeeMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, TreeSet<Employee>> pair = (Map.Entry<String, TreeSet<Employee>>) iterator.next();
			String l_key = (String) pair.getKey();
			TreeSet<Employee> l_empList = (TreeSet<Employee>) pair.getValue();
			allEMP.addAll(l_empList);
		}

		for (Employee employee : allEMP) {
			String desig = employee.designation;
			for (Job job : employee.jobList) {
				desig += "#" + job.jobid;
			}
			l_outputArr.add(desig);
		}
		String[] result = new String[l_outputArr.size()];
		result = l_outputArr.toArray(result);
		return result;
	}
}

class Job implements Comparable<Job> {
	int priority;
	int time;
	String jobid;
	String skill;

	public Job(int priority, int time, String jobid, String skill) {
		this.priority = priority;
		this.time = time;
		this.jobid = jobid;
		this.skill = skill;
	}

	@Override
	public int compareTo(Job o) {
		if (this.jobid.equals(o.jobid))
			return 0;

		if (!this.skill.equals(o.skill))
			return -1;

		if (this.priority > o.priority)
			return -1;
		else if (this.priority < o.priority)
			return 1;
		else if (this.time > o.time)
			return 1;
		else if (this.time < o.time)
			return -1;
		else
			return 1;
	}

	@Override
	public String toString() {
		return "Job [priority=" + priority + ", time=" + time + ", jobid=" + jobid + ", skill=" + skill + "]";
	}
}

class Employee implements Comparable<Employee> {
	String designation;
	String skill;
	TreeSet<Job> jobList;
	int totalhours;

	public Employee(String designation, String skill) {
		super();
		this.designation = designation;
		this.skill = skill;
		jobList = new TreeSet<>();
	}

	@Override
	public int compareTo(Employee o) {
		if (this.totalhours < o.totalhours)
			return -1;
		else if (this.totalhours > o.totalhours)
			return 1;
		return this.designation.compareTo(o.designation);
	}

	@Override
	public String toString() {
		return "Employee [designation=" + designation + ", skill=" + skill + ", jobList=" + jobList + ", totalhours=" + totalhours + "]";
	}
}