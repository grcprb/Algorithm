package com.test.contest;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
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
		HashMap<String, TreeSet<Employee>> employeeMap = new HashMap<String, TreeSet<Employee>>();
		HashMap<String, TreeSet<Job>> jobMap = new HashMap<String, TreeSet<Job>>();

		TreeSet<Employee> allEMP = new TreeSet<>(new Comparator<Employee>() {

			@Override
			public int compare(Employee o1, Employee o2) {
				return o1.designation.compareTo(o2.designation);
			}
		});

		for (int i = 0; i < input1.length; i++) {
			String[] split = input1[i].split("#");
			Employee employee = new Employee(split[0], split[1]);
			TreeSet<Employee> empSet = employeeMap.get(employee.skill);
			if (empSet != null) {
				empSet.add(employee);
			} else {
				empSet = new TreeSet<Employee>();
				empSet.add(employee);
				employeeMap.put(employee.skill, empSet);
			}
			allEMP.add(employee);
		}

		for (int i = 0; i < input2.length; i++) {
			String[] split = input2[i].split("#");
			Job job = new Job(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3], split[0]);
			TreeSet<Job> jobSet = jobMap.get(job.skill);
			if (jobSet != null) {
				jobSet.add(job);
			} else {
				jobSet = new TreeSet<Job>();
				jobSet.add(job);
				jobMap.put(job.skill, jobSet);
			}
		}

		/*
		 * for (Iterator<Entry<String, TreeSet<Employee>>> iterator = l_employeeMap.entrySet().iterator(); iterator.hasNext();) { Map.Entry<String, TreeSet<Employee>> pair = (Map.Entry<String, TreeSet<Employee>>) iterator.next(); String l_key = (String) pair.getKey(); TreeSet<Employee> l_empList = (TreeSet<Employee>) pair.getValue(); System.out.println(l_key); for (Iterator iterator2 = l_empList.iterator(); iterator2.hasNext();) { Employee employee = (Employee) iterator2.next(); System.out.println(employee); } } for (Iterator<Entry<String, TreeSet<Job>>> iterator = l_jobMap.entrySet().iterator(); iterator.hasNext();) { Map.Entry<String, TreeSet<Job>> pair = (Map.Entry<String, TreeSet<Job>>) iterator.next(); String l_key = (String) pair.getKey(); TreeSet<Job> l_jobList = (TreeSet<Job>) pair.getValue(); System.out.println(l_key); for (Iterator iterator2 = l_jobList.iterator(); iterator2.hasNext();) { Job job = (Job) iterator2.next(); System.out.println(job); } }
		 */

		Set<Entry<String, TreeSet<Job>>> jobEntrySet = jobMap.entrySet();
		for (Entry<String, TreeSet<Job>> entry : jobEntrySet) {
			TreeSet<Job> jobSkillSet = entry.getValue();
			for (Job job : jobSkillSet) {
				TreeSet<Employee> empSkillSet = employeeMap.get(job.skill);
				Employee emp = empSkillSet.first();
				empSkillSet.remove(emp);
				emp.jobList.add(job);
				emp.totalhours = emp.totalhours + job.time;
				empSkillSet.add(emp);
				/*
				 * System.out.println("-----------------------------------"); for (Iterator iterator2 = empSkillSet.iterator(); iterator2.hasNext();) { Employee employee = (Employee) iterator2.next(); System.out.println(employee); }
				 */
			}
		}

		String[] result = new String[allEMP.size()];
		int i = 0;
		for (Employee employee : allEMP) {
			String desig = employee.designation;
			for (Job job : employee.jobList) {
				desig += "#" + job.jobid;
			}
			result[i++] = desig;
		}
		return result;
	}

	static class Job implements Comparable<Job> {
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

	static class Employee implements Comparable<Employee> {
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
}