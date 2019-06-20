import os
import matplotlib.pyplot as plt
import numpy as np
from collections import Counter


def sorted_avg(sort_key, value):
    total_sum = Counter()
    counter = Counter()
    for i in range(len(sort_key)):
        total_sum[sort_key[i]] += value[i]
        counter[sort_key[i]] += 1
    total_sum = np.array(sorted(total_sum.items()))[:, 1]
    counter = np.array(sorted(counter.items()))[:, 1]
    return total_sum / counter


data_dir = input("Input path to data directory: ")
output_dir = "img/"
if not os.path.isdir(output_dir):
    os.mkdir(output_dir)


def load_file(name):
    file = open(os.path.join(data_dir, name))
    values = file.readline()[:-1].split(" ")
    file.close()
    return np.array([float(value) for value in values])


build_e = load_file("tree_build_time_e")
nodes_e = load_file("nodes_e")
size_e = [2.0**i for i in range(10, 10 + len(build_e))]

len_e = load_file("lenP_e")
unique_len_e = sorted(np.unique(len_e))
count_e = load_file("count_time_e")
avg_count_e = sorted_avg(len_e, count_e)
locate_e = load_file("locate_time_e")
avg_locate_e = sorted_avg(len_e, locate_e)

k_e = load_file("k_e")
unique_k_e = sorted(np.unique(k_e))
q_e = load_file("q_e")
unique_q_e = sorted(np.unique(q_e))
topkq_e = load_file("topk_time_e")
avg_topkq_per_k_e = sorted_avg(k_e, topkq_e)
avg_topkq_per_q_e = sorted_avg(q_e, topkq_e)

build_d = load_file("tree_build_time_d")
nodes_d = load_file("nodes_d")
size_d = [2.0**i for i in range(10, 10 + len(build_d))]

len_d = load_file("lenP_d")
unique_len_d = sorted(np.unique(len_d))
count_d = load_file("count_time_d")
avg_count_d = sorted_avg(len_d, count_d)
locate_d = load_file("locate_time_d")
avg_locate_d = sorted_avg(len_d, locate_d)

k_d = load_file("k_d")
unique_k_d = sorted(np.unique(k_d))
q_d = load_file("q_d")
unique_q_d = sorted(np.unique(q_d))
topkq_d = load_file("topk_time_d")
avg_topkq_per_k_d = sorted_avg(k_d, topkq_d)
avg_topkq_per_q_d = sorted_avg(q_d, topkq_d)

plt.clf()
plt.loglog(size_e, build_e / 10**6, '*--', basex=2, basey=2)
plt.loglog(size_d, build_d / 10**6, '*--', basex=2, basey=2)
plt.ylim(1, plt.ylim()[1])
plt.legend(['English text', 'DNA sequence'])
plt.xlabel("Length of text [# of characters]")
plt.ylabel("Time to build Suffix Tree [ms]")
plt.tight_layout()
plt.savefig(output_dir + "build_time.png")

plt.clf()
plt.loglog(size_e, nodes_e, '*--', basex=2, basey=2)
plt.loglog(size_d, nodes_d, '*--', basex=2, basey=2)
plt.ylim(2**9, plt.ylim()[1])
plt.legend(['English text', 'DNA sequence'])
plt.xlabel("Length of text [# of characters]")
plt.ylabel("Size of Suffix Tree [# of nodes]")
plt.tight_layout()
plt.savefig(output_dir + "nodes.png")

plt.clf()
plt.plot(unique_k_e[1:], avg_topkq_per_k_e[1:] / 10**6, '*--')
plt.plot(unique_k_d[1:], avg_topkq_per_k_d[1:] / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.legend(['English text', 'DNA sequence'])
plt.xlabel("k value")
plt.ylabel("Average time of top-k-q [ms]")
plt.tight_layout()
plt.savefig(output_dir + "topkq_k_time.png")

plt.clf()
plt.plot(unique_q_e, avg_topkq_per_q_e / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.legend(['English text'])
plt.xlabel("q value")
plt.ylabel("Average time of top-k-q [ms]")
plt.tight_layout()
plt.savefig(output_dir + "topkq_q_time_e.png")

plt.clf()
plt.plot(unique_q_d, avg_topkq_per_q_d / 10**6, '*--', color='orange')
plt.ylim(0, plt.ylim()[1])
plt.legend(['DNA sequence'])
plt.xlabel("q value")
plt.ylabel("Average time of top-k-q [ms]")
plt.tight_layout()
plt.savefig(output_dir + "topkq_q_time_d.png")


plt.clf()
plt.plot(unique_len_e, avg_count_e / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.xlabel("Length of word [# of characters]")
plt.ylabel("Average time of count [ns]")
plt.tight_layout()
plt.savefig(output_dir + "count_time_e.png")

plt.clf()
plt.plot(unique_len_e, avg_locate_e / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.xlabel("Length of word [# of characters]")
plt.ylabel("Average time of locate [ns]")
plt.tight_layout()
plt.savefig(output_dir + "locate_time_e.png")


plt.clf()
plt.plot(unique_len_d, avg_count_d / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.xlabel("Length of word [# of characters]")
plt.ylabel("Average time of count [ns]")
plt.tight_layout()
plt.savefig(output_dir + "count_time_d.png")

plt.clf()
plt.plot(unique_len_d, avg_locate_d / 10**6, '*--')
plt.ylim(0, plt.ylim()[1])
plt.xlabel("Length of word [# of characters]")
plt.ylabel("Average time of locate [ns]")
plt.tight_layout()
plt.savefig(output_dir + "locate_time_d.png")
